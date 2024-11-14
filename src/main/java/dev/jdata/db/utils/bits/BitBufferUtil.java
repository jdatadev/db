package dev.jdata.db.utils.bits;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public class BitBufferUtil {

    public static boolean isNumBitsOnByteBoundary(int numBits) {

        return (numBits & 0x7) == 0;
    }

    public static boolean isBitOffsetOnByteBoundary(long bitOffset) {

        return (bitOffset & 0x7L) == 0L;
    }

    public static int numBytes(long startBitOffset, long endBitOffset) {

        Checks.isOffset(startBitOffset);
        Checks.isOffset(endBitOffset);

        if (endBitOffset <= startBitOffset) {

            throw new IllegalArgumentException();
        }

        return Integers.checkUnsignedLongToUnsignedInt((((endBitOffset + 1) & 0x7L) - (startBitOffset & 0x7L)) / 8) + 1;
    }

    public static int numBytes(long numBits) {

        long numBytes = (numBits / 8);

        if ((numBits & 0x7L) != 0) {

            ++ numBytes;
        }

        if (numBytes > Integer.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        return (int)numBytes;
    }

    public static int numBytesExact(int numBits) {

        if (!isNumBitsOnByteBoundary(numBits)) {

            throw new IllegalArgumentException();
        }

        return numBits / 8;
    }

    public static int numLeftoverBits(long bitOffset) {

        return (int)(bitOffset & 0x7L);
    }

    public static int setIntValue(byte[] buffer, long bufferBitOffset, int numBits, int value, boolean signed) {

        return setLongValue(buffer, bufferBitOffset, numBits, value, signed);
    }

    public static int setLongValue(byte[] buffer, long bufferBitOffset, int numBits, long value, boolean signed) {

        if (signed) {

            throw new UnsupportedOperationException();
        }

        if (!signed && value < 0) {

            throw new IllegalArgumentException();
        }

        final int valueNumBits = BitsUtil.getNumBits(value, signed);

        if (valueNumBits > numBits) {

            throw new IllegalArgumentException();
        }
        else if (signed && valueNumBits == numBits) {

            throw new IllegalArgumentException();
        }

        int remaining = valueNumBits;

        int bufferByteOffset = (int)(bufferBitOffset / 8);

        int remainingBitsOfOutputByte = 8 - (int)(bufferBitOffset & 0x7L);

        do {
            final int bitsToUpdate;

            if (remaining <= remainingBitsOfOutputByte) {

                bitsToUpdate = remaining;

                remaining = 0;
            }
            else {
                bitsToUpdate = remainingBitsOfOutputByte;

                remaining -= bitsToUpdate;
            }

            final byte existingByte = buffer[bufferByteOffset];
            final int updateLeftShift = 8 - bitsToUpdate;

            final boolean set = (value & (1L << remaining)) != 0L;

            if (set) {

                final byte setMask = (byte)(BitsUtil.mask(bitsToUpdate) << updateLeftShift);

                buffer[bufferByteOffset] = (byte)(existingByte & setMask);
            }
            else {
                final byte clearMask = (byte)(~(BitsUtil.mask(bitsToUpdate) << updateLeftShift));

                buffer[bufferByteOffset] = (byte)(existingByte & clearMask);
            }

        } while (remaining != 0);

        return valueNumBits;
    }

    public static boolean isBitSet(byte[] buffer, long bufferBitOffset) {

        final int byteOffset = Integers.checkUnsignedLongToUnsignedInt(bufferBitOffset / 8);

        final int bitOffsetWithinByte = 8 - (int)(bufferBitOffset & 0x7L);

        return (buffer[byteOffset] & (1 << bitOffsetWithinByte)) != 0;
    }

    public static void setBitValue(byte[] buffer, long bufferBitOffset, boolean set) {

        final int bufferByteOffset = Integers.checkUnsignedLongToUnsignedInt(bufferBitOffset / 8);
        final int bitOffsetWithinByte = 8 - (int)(bufferBitOffset & 0x7L);

        final byte mask = (byte)(1 << (bitOffsetWithinByte - 1));

        final byte existingByte = buffer[bufferByteOffset];

        buffer[bufferByteOffset] = (byte)(set ? (existingByte | mask) : (existingByte & (~mask)));
    }

    public static void setBits(byte[] buffer, long bufferBitOffset, long numBits, boolean set) {

        long remaining = numBits;

        int bufferByteOffset = (int)(bufferBitOffset / 8);

        int remainingBitsOfOutputByte = 8 - (int)(bufferBitOffset & 0x7L);

        do {
            final int bitsToUpdate;

            if (remaining <= remainingBitsOfOutputByte) {

                bitsToUpdate = (int)remaining;

                remaining = 0;
            }
            else {
                bitsToUpdate = remainingBitsOfOutputByte;

                remaining -= bitsToUpdate;
            }

            final byte existingByte = buffer[bufferByteOffset];
            final int updateLeftShift = 8 - bitsToUpdate;

            if (set) {

                final byte setMask = (byte)(BitsUtil.mask(bitsToUpdate) << updateLeftShift);

                buffer[bufferByteOffset] = (byte)(existingByte & setMask);
            }
            else {
                final byte clearMask = (byte)(~(BitsUtil.mask(bitsToUpdate) << updateLeftShift));

                buffer[bufferByteOffset] = (byte)(existingByte & clearMask);
            }

        } while (remaining != 0);
    }

    public static void clearBits(byte[] buffer, long bufferBitOffset, long numBits) {

        long remaining = numBits;

        int bufferByteOffset = (int)(bufferBitOffset / 8);

        int remainingBitsOfOutputByte = 8 - (int)(bufferBitOffset & 0x7L);

        do {
            final int bitsToClear;

            if (remaining <= remainingBitsOfOutputByte) {

                bitsToClear = (int)remaining;

                remaining = 0;
            }
            else {
                bitsToClear = remainingBitsOfOutputByte;

                remaining -= bitsToClear;
            }

            final byte existingByte = buffer[bufferByteOffset];
            final int clearLeftShift = 8 - bitsToClear;

            final byte clearMask = (byte)(~(BitsUtil.mask(bitsToClear) << clearLeftShift));

            buffer[bufferByteOffset] = (byte)(existingByte & clearMask);

        } while (remaining != 0);
    }

    public static void copyBits(byte[] inputBuffer, long inputBufferBitOffset, long numInputBits, byte[] outputBuffer, long outputBufferBitOffset, long numOutputBits) {

        final long inputBitOffset;
        final long outputBitOffset;

        final long totalNumBits;

        if (numInputBits > numOutputBits) {

            throw new IllegalArgumentException();
        }
        else if (numInputBits < numOutputBits) {

            final long numZeroOutputBits = numOutputBits - numInputBits;

            clearBits(outputBuffer, outputBufferBitOffset, numZeroOutputBits);

            inputBitOffset = inputBufferBitOffset;
            outputBitOffset = outputBufferBitOffset;
            totalNumBits = numInputBits;
        }
        else {
            inputBitOffset = inputBufferBitOffset;
            outputBitOffset = outputBufferBitOffset;
            totalNumBits = numInputBits;
        }

        if (totalNumBits <= 8) {

            copyLessOrEqualToEightBits(inputBuffer, inputBitOffset, outputBuffer, outputBitOffset, (int)totalNumBits);
        }
        else {
            copyMoreThanEightBits(inputBuffer, inputBitOffset, outputBuffer, outputBitOffset, totalNumBits);
        }
    }

    private static void copyLessOrEqualToEightBits(byte[] inputBuffer, long inputBitOffset, byte[] outputBuffer, long outputBitOffset, int totalNumBits) {

        final int inputBufferByteOffset = (int)(inputBitOffset / 8);
        final int outputBufferByteOffset = (int)(outputBitOffset / 8);

        final int numInputBits1 = 8 - (int)(inputBitOffset & 0x7L);
        final int numOutputBits1 = 8 - (int)(outputBitOffset & 0x7L);

        final byte inputBits;

        if (numInputBits1 >= totalNumBits) {

            final int shift = numInputBits1 - totalNumBits;

            inputBits = retrieve(inputBuffer[inputBufferByteOffset], shift, totalNumBits);
        }
        else {
            final int numInputBits2 = totalNumBits - numInputBits1;

            inputBits = (byte)(
                     (retrieve(inputBuffer[inputBufferByteOffset], numInputBits1) << totalNumBits)
                    | retrieve(inputBuffer[inputBufferByteOffset + 1], 8 - numInputBits2, numInputBits2));
        }

        if (numOutputBits1 >= totalNumBits) {

            final byte existingByte = outputBuffer[outputBufferByteOffset];
            final int inputLeftShift = numOutputBits1 - totalNumBits;
            final byte clearMask = (byte)~(BitsUtil.mask(totalNumBits) << inputLeftShift);

            outputBuffer[outputBufferByteOffset] = (byte)((existingByte & clearMask) | (inputBits << inputLeftShift));
        }
        else {
            final int numOutputBits2 = totalNumBits - numOutputBits1;

            final byte existingByte1 = outputBuffer[outputBufferByteOffset];
            final byte clearMask1 = (byte)~BitsUtil.mask(numOutputBits1);
            final int inputLeftShift1 = numOutputBits2;

            outputBuffer[outputBufferByteOffset] = (byte)((existingByte1 & clearMask1) | (inputBits << inputLeftShift1));

            final int outputBufferByteOffset2 = outputBufferByteOffset + 1;
            final byte existingByte2 = outputBuffer[outputBufferByteOffset2];
            final byte clearMask2 = (byte)~(BitsUtil.mask(numOutputBits2) << (8 - numOutputBits2));

            outputBuffer[outputBufferByteOffset2] = (byte)((existingByte2 & clearMask2) | (inputBits & BitsUtil.mask(numOutputBits2)));
        }
    }

    private static void copyMoreThanEightBits(byte[] inputBuffer, long startInputBitOffset, byte[] outputBuffer, long startOutputBitOffset, long totalNumBits) {

        long inputBitOffset = startInputBitOffset;
        long outputBitOffset = startOutputBitOffset;

        long remainingBits = totalNumBits;

        int inputBufferByteOffset = (int)(inputBitOffset / 8);
        int outputBufferByteOffset = (int)(outputBitOffset / 8);

        int remainingBitsOfInputByte = 8 - (int)(inputBitOffset & 0x7L);
        int remainingBitsOfOutputByte = 8 - (int)(outputBitOffset & 0x7L);

        do {
            final byte existingByte = outputBuffer[outputBufferByteOffset];

            if (remainingBits <= remainingBitsOfInputByte) {

                final int intRemainingBits = (int)remainingBits;

                if (remainingBits <= remainingBitsOfOutputByte) {

                    final int bitsToCopy = intRemainingBits;

                    final int inputRightShift = 8 - bitsToCopy;
                    final byte inputBits = retrieve(inputBuffer[inputBufferByteOffset], inputRightShift, bitsToCopy);

                    final int inputLeftShift = remainingBitsOfOutputByte - bitsToCopy;
                    final byte existingByteClearMask = (byte)~(BitsUtil.mask(bitsToCopy) << inputLeftShift);

                    outputBuffer[outputBufferByteOffset] = (byte)((existingByte & existingByteClearMask) | (inputBits << inputLeftShift));

                    remainingBits = 0;
                }
                else {
                    final int bitsToCopy1 = remainingBitsOfOutputByte;

                    final int inputRightShift = remainingBitsOfInputByte - bitsToCopy1;
                    final byte inputBits = retrieve(inputBuffer[inputBufferByteOffset], inputRightShift, bitsToCopy1);

                    remainingBits -= bitsToCopy1;

                    final int inputLeftShift1 = remainingBitsOfOutputByte - bitsToCopy1;
                    final byte existingByteClearMask = (byte)~(BitsUtil.mask(bitsToCopy1) << inputLeftShift1);

                    outputBuffer[outputBufferByteOffset] = (byte)((existingByte & existingByteClearMask) | (inputBits << inputLeftShift1));

                    final int bitsToCopy2 = intRemainingBits - remainingBitsOfOutputByte;

                    final int outputBufferByteOffset2 = outputBufferByteOffset + 1;
                    final byte existingByte2 = outputBuffer[outputBufferByteOffset2];
                    final int inputLeftShift2 = 8 - bitsToCopy2;
                    final byte existingByteClearMask2 = (byte)~(BitsUtil.mask(bitsToCopy2) << inputLeftShift1);

                    outputBuffer[outputBufferByteOffset2] = (byte)((existingByte2 & existingByteClearMask2) | (inputBits << inputLeftShift2));
                }
            }
            else {
                if (remainingBitsOfInputByte < remainingBitsOfOutputByte) {

                    final int bitsToCopy = remainingBitsOfInputByte;

                    final byte inputBits = retrieve(inputBuffer[inputBufferByteOffset], bitsToCopy);

                    final int inputLeftShift = remainingBitsOfOutputByte - bitsToCopy;
                    final byte existingByteClearMask = (byte)~(BitsUtil.mask(bitsToCopy) << inputLeftShift);

                    outputBuffer[outputBufferByteOffset] = (byte)((existingByte & existingByteClearMask) | (inputBits << inputLeftShift));

                    remainingBitsOfInputByte = 8;
                    ++ inputBufferByteOffset;

                    remainingBitsOfOutputByte -= bitsToCopy;
                }
                else if (remainingBitsOfInputByte == remainingBitsOfOutputByte) {

                    final int bitsToCopy = remainingBitsOfInputByte;

                    final byte inputBits = retrieve(inputBuffer[inputBufferByteOffset], bitsToCopy);

                    final int inputLeftShift = remainingBitsOfOutputByte - bitsToCopy;
                    final byte existingByteClearMask = (byte)~(BitsUtil.mask(bitsToCopy) << inputLeftShift);

                    outputBuffer[outputBufferByteOffset] = (byte)((existingByte & existingByteClearMask) | (inputBits << inputLeftShift));

                    remainingBitsOfOutputByte = 8;
                    ++ outputBufferByteOffset;

                    remainingBitsOfOutputByte = 8;
                    ++ outputBufferByteOffset;
                }
                else {
                    final int bitsToCopy = remainingBitsOfOutputByte;

                    final int inputRightShift = remainingBitsOfInputByte - bitsToCopy;

                    final byte inputBits = retrieve(inputBuffer[inputBufferByteOffset], inputRightShift, bitsToCopy);

                    final byte existingByteClearMask = (byte)~BitsUtil.mask(bitsToCopy);

                    outputBuffer[outputBufferByteOffset] = (byte)((existingByte & existingByteClearMask) | inputBits);

                    remainingBitsOfInputByte -= bitsToCopy;

                    remainingBitsOfOutputByte = 8;
                    ++ outputBufferByteOffset;
                }
            }
        } while (remainingBits != 0L);
    }

    private static byte retrieve(byte source, int numBits) {

        return (byte)(source & BitsUtil.mask(numBits));
    }

    private static byte retrieve(byte source, int shift, int numBits) {

        return (byte)((source >>> shift) & BitsUtil.mask(numBits));
    }
}
