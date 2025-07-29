package dev.jdata.db.utils.bits;

import java.util.function.ToIntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.function.ByteGetter;
import dev.jdata.db.utils.math.Sign;
import dev.jdata.db.utils.scalars.Integers;

public class BitBufferUtil {

    private static final boolean DEBUG = DebugConstants.DEBUG_BIT_BUFFER_UTIL;

    private static final Class<?> debugClass = BitBufferUtil.class;

    public static boolean isNumBitsOnByteBoundary(long numBits) {

        return (numBits & 0x7L) == 0;
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

        return Integers.checkUnsignedLongToUnsignedInt((((endBitOffset + 1) & 0x7L) - (startBitOffset & 0x7L)) >>> 3) + 1;
    }

    public static int numBytes(long numBits) {

        long numBytes = (numBits >>> 3);

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

        return numBits >>> 3;
    }

    public static long numBytesExact(long numBits) {

        if (!isNumBitsOnByteBoundary(numBits)) {

            throw new IllegalArgumentException();
        }

        return numBits >>> 3;
    }

    public static int byteOffsetExact(int bitOffset) {

        if (!isBitOffsetOnByteBoundary(bitOffset)) {

            throw new IllegalArgumentException();
        }

        return bitOffset >>> 3;
    }

    public static long byteOffsetExact(long bitOffset) {

        if (!isBitOffsetOnByteBoundary(bitOffset)) {

            throw new IllegalArgumentException();
        }

        return bitOffset >>> 3;
    }

    public static int numLeftoverBits(long bitOffset) {

        final int masked = (int)(bitOffset & 0x7L);

        return masked != 0 ? 8 - masked : 0;
    }

    public static int getShortValue(byte[] buffer, boolean signed, long bufferBitOffset, int numBits) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).add("signed", signed).add("bufferBitOffset", bufferBitOffset).add("numBits", numBits));
        }

        final int result = (int)getLongValue(buffer, signed, bufferBitOffset, numBits, 16);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result);
        }

        return result;
    }

    public static int setShortValue(byte[] buffer, short value, boolean signed, long bufferBitOffset, int numBits) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).binary("value", value).add("signed", signed).add("bufferBitOffset", bufferBitOffset)
                    .add("numBits", numBits));
        }

        final int result = setLongValue(buffer, value, signed, bufferBitOffset, numBits, 16);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    public static int getIntValue(byte[] buffer, boolean signed, long bufferBitOffset, int numBits) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).add("signed", signed).add("bufferBitOffset", bufferBitOffset).add("numBits", numBits));
        }

        final int result = (int)getLongValue(buffer, signed, bufferBitOffset, numBits, 32);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result);
        }

        return result;
    }

    public static int setIntValue(byte[] buffer, int value, boolean signed, long bufferBitOffset, int numBits) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).binary("value", value).add("signed", signed).add("bufferBitOffset", bufferBitOffset)
                    .add("numBits", numBits));
        }

        final int result = setLongValue(buffer, value, signed, bufferBitOffset, numBits, 32);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    public static long getLongValue(byte[] buffer, boolean signed, long bufferBitOffset, int numBits) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).add("signed", signed).add("bufferBitOffset", bufferBitOffset).add("numBits", numBits));
        }

        final long result = getLongValue(buffer, signed, bufferBitOffset, numBits, 64);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result);
        }

        return result;
    }

    private static long getLongValue(byte[] buffer, boolean signed, long bufferBitOffset, int numBits, int numBitsToScan) {

        checkBuffer(buffer, signed, bufferBitOffset, numBits, numBitsToScan);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).add("signed", signed).add("bufferBitOffset", bufferBitOffset).add("numBits", numBits)
                    .add("numBitsToScan", numBitsToScan));
        }

        final Sign sign;

        long bitOffset = bufferBitOffset;

        int remainingBits = numBits;

        if (signed) {

            sign = BitBufferUtil.isBitSet(buffer, bitOffset) ? Sign.MINUS : Sign.PLUS;

            ++ bitOffset;
            -- remainingBits;
        }
        else {
            sign = Sign.PLUS;
        }

        int bufferByteOffset = (int)(bitOffset >>> 3);

        int remainingBitsOfInputByte = 8 - (int)(bitOffset & 0x7L);

        long unsignedValue = 0;

        if (DEBUG) {

            final int r = remainingBitsOfInputByte;
            final int o = bufferByteOffset;

            PrintDebug.debug(debugClass, "at start of loop", b -> b.add("bufferByteOffset", o).add("remainingBitsOfInputByte", r));
        }

        do {
            final byte existingByte = buffer[bufferByteOffset];

            if (DEBUG) {

                final int o = bufferByteOffset;
                final int r1 = remainingBits;
                final int r2 = remainingBitsOfInputByte;
                final long u = unsignedValue;

                PrintDebug.debug(debugClass, b -> b.add("bufferByteOffset", o).binary("existingByte", existingByte).add("remainingBits", r1).add("remainingBitsOfInputByte", r2)
                        .binary("unsignedValue", u));
            }

            if (remainingBits <= remainingBitsOfInputByte) {

                unsignedValue <<= remainingBits;

                unsignedValue |= BitsUtil.getRange(existingByte, remainingBitsOfInputByte - 1, remainingBits);

                remainingBits = 0;
            }
            else {
                final int numBitsToRetrieve = remainingBitsOfInputByte;

                unsignedValue <<= numBitsToRetrieve;

                unsignedValue |= BitsUtil.getRange(existingByte, remainingBitsOfInputByte - 1, numBitsToRetrieve);

                remainingBits -= numBitsToRetrieve;

                ++ bufferByteOffset;

                remainingBitsOfInputByte = 8;
            }

        } while (remainingBits != 0);

        final long result = sign == Sign.PLUS ? unsignedValue : - unsignedValue;

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result);
        }

        return result;
    }

    public static int setLongValue(byte[] buffer, long value, boolean signed, long bufferBitOffset, int numBits) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).binary("value", value).add("signed", signed).add("bufferBitOffset", bufferBitOffset)
                    .add("numBits", numBits));
        }

        final int result = setLongValue(buffer, value, signed, bufferBitOffset, numBits, 64);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static int setLongValue(byte[] buffer, long value, boolean signed, long bufferBitOffset, int numBits, int numBitsToScan) {

        checkBuffer(buffer, signed, bufferBitOffset, numBits, numBitsToScan);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).binary("value", value).add("signed", signed).add("bufferBitOffset", bufferBitOffset)
                    .add("numBits", numBits).add("numBitsToScan", numBitsToScan));
        }

        if (signed) {

            throw new UnsupportedOperationException();
        }
        else {
            if (value < 0L) {

                throw new IllegalArgumentException();
            }
        }

        final int valueNumBits = BitsUtil.getNumStorageBits(value, signed, numBitsToScan);

        if (valueNumBits > numBits) {

            throw new IllegalArgumentException();
        }
        else if (signed && valueNumBits == numBits) {

            throw new IllegalArgumentException();
        }

        int remainingBits = numBits;

        int bufferByteOffset = (int)(bufferBitOffset >>> 3);

        int remainingBitsOfOutputByte = 8 - (int)(bufferBitOffset & 0x7L);

        if (DEBUG) {

            final int r = remainingBitsOfOutputByte;
            final int o = bufferByteOffset;

            PrintDebug.debug(debugClass, "at start of loop", b -> b.add("bufferByteOffset", o).add("remainingBitsOfOutputByte", r));
        }

        do {
            final int numBitsToUpdate;

            if (remainingBits <= remainingBitsOfOutputByte) {

                numBitsToUpdate = remainingBits;

                remainingBits = 0;
            }
            else {
                numBitsToUpdate = remainingBitsOfOutputByte;

                remainingBits -= numBitsToUpdate;
            }

            final byte existingByte = buffer[bufferByteOffset];

            final byte bitsFromValue = (byte)BitsUtil.getRange(value, remainingBits + numBitsToUpdate - 1, numBitsToUpdate);

            final byte updatedByte;

            if (remainingBitsOfOutputByte == 8) {

                if (numBitsToUpdate == 8) {

                    updatedByte = bitsFromValue;
                }
                else {
                    final int endShift = 8 - numBitsToUpdate;

                    updatedByte = (byte)((bitsFromValue << endShift) | (existingByte & BitsUtil.mask(endShift)));
                }
            }
            else {
                final int numBeginBits = 8 - remainingBitsOfOutputByte;
                final int numEndBits = 8 - numBeginBits - numBitsToUpdate;

                final byte beginMask = (byte)(BitsUtil.mask(numBeginBits) << remainingBitsOfOutputByte);
                final byte endMask = BitsUtil.mask(numEndBits);

                updatedByte = (byte)((existingByte & (beginMask | endMask)) | (bitsFromValue << numEndBits));

                remainingBitsOfOutputByte = 8;
            }

            if (DEBUG) {

                final int r1 = remainingBits;
                final int r2 = remainingBitsOfOutputByte;

                PrintDebug.debug(debugClass, "computed updated byte", b -> b.add("remainingBits", r1).add("numBitsToUpdate", numBitsToUpdate)
                        .add("remainingBitsOfOutputByte", r2).binary("bitsFromValue", bitsFromValue).binary("existingByte", existingByte).binary("updatedByte", updatedByte));
            }

            buffer[bufferByteOffset ++] = updatedByte;

        } while (remainingBits != 0);

        if (DEBUG) {

            PrintDebug.exit(debugClass, valueNumBits);
        }

        return valueNumBits;
    }

    public static boolean isBitSet(byte[] buffer, long bufferBitOffset) {

        final int byteOffset = Integers.checkUnsignedLongToUnsignedInt(bufferBitOffset >>> 3);

        final int bitOffsetWithinByte = 7 - (int)(bufferBitOffset & 0x7L);

        return (buffer[byteOffset] & (1 << bitOffsetWithinByte)) != 0;
    }

    public static void setBitValue(byte[] buffer, boolean set, long bufferBitOffset) {

        final int bufferByteOffset = Integers.checkUnsignedLongToUnsignedInt(bufferBitOffset >>> 3);
        final int bitOffsetWithinByte = 7 - (int)(bufferBitOffset & 0x7L);

        final byte bit = (byte)(1 << bitOffsetWithinByte);

        final byte existingByte = buffer[bufferByteOffset];

        buffer[bufferByteOffset] = (byte)(set ? (existingByte | bit) : (existingByte & (~bit)));
    }

    public static void setBits(byte[] buffer, boolean set, long bufferBitOffset, long numBits) {

        checkBuffer(buffer, bufferBitOffset, numBits);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).add("set", set).add("bufferBitOffset", bufferBitOffset).add("numBits", numBits));
        }

        long remaining = numBits;

        int bufferByteOffset = (int)(bufferBitOffset >>> 3);

        int bitOffsetIntoByte = (int)(bufferBitOffset & 0x7L);

        int remainingBitsOfOutputByte = 8 - bitOffsetIntoByte;

        do {
            final int numBitsToUpdate;

            if (remaining <= remainingBitsOfOutputByte) {

                numBitsToUpdate = (int)remaining;

                remaining = 0;
            }
            else {
                numBitsToUpdate = remainingBitsOfOutputByte;

                remaining -= numBitsToUpdate;
            }

            final byte existingByte = buffer[bufferByteOffset];
            final int updateLeftShift = 8 - bitOffsetIntoByte - numBitsToUpdate;

            final byte mask = BitsUtil.mask(numBitsToUpdate);

            final byte updatedByte;

            if (set) {

                final byte setMask = (byte)(mask << updateLeftShift);

                updatedByte = (byte)(existingByte | setMask);
            }
            else {
                final byte clearMask = (byte)(~(mask << updateLeftShift));

                updatedByte = (byte)(existingByte & clearMask);
            }

            if (DEBUG) {

                final long r1 = remaining;
                final int r2 = remainingBitsOfOutputByte;
                final int o = bitOffsetIntoByte;

                PrintDebug.debug(debugClass, b -> b.add("remaining", r1).add("remainingBitsOfOutputByte", r2).binary("existingByte", existingByte).add("bitOffsetIntoByte", o)
                        .add("numBitsToUpdate", numBitsToUpdate).add("updateLeftShift", updateLeftShift).binary("mask", mask).binary("updatedByte", updatedByte));
            }

            buffer[bufferByteOffset ++] = updatedByte;

            remainingBitsOfOutputByte = 8;
            bitOffsetIntoByte = 0;

        } while (remaining != 0);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    public static void clearBits(byte[] buffer, long bufferBitOffset, long numBits) {

        checkBuffer(buffer, bufferBitOffset, numBits);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("buffer.length", buffer.length).add("bufferBitOffset", bufferBitOffset).add("numBits", numBits));
        }

        long remaining = numBits;

        int bufferByteOffset = (int)(bufferBitOffset >>> 3);

        int bitOffsetIntoByte = (int)(bufferBitOffset & 0x7L);

        int remainingBitsOfOutputByte = 8 - bitOffsetIntoByte;

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
            final int clearLeftShift = 8 - bitOffsetIntoByte - bitsToClear;

            final byte clearMask = (byte)(~(BitsUtil.mask(bitsToClear) << clearLeftShift));

            final byte updatedByte = (byte)(existingByte & clearMask);

            if (DEBUG) {

                final long r1 = remaining;
                final int r2 = remainingBitsOfOutputByte;
                final int o = bitOffsetIntoByte;

                PrintDebug.debug(debugClass, b -> b.add("remaining", r1).add("remainingBitsOfOutputByte", r2).binary("existingByte", existingByte).add("bitOffsetIntoByte", o)
                        .add("bitsToClear", bitsToClear).add("clearLeftShift", clearLeftShift).binary("clearMask", clearMask).binary("updatedByte", updatedByte));
            }

            buffer[bufferByteOffset ++] = updatedByte;

            remainingBitsOfOutputByte = 8;
            bitOffsetIntoByte = 0;

        } while (remaining != 0);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    public static void copyBits(byte[] inputBuffer, long inputBufferBitOffset, long numInputBits, byte[] outputBuffer, long outputBufferBitOffset, long numOutputBits) {

        copyBits(inputBuffer, inputBufferBitOffset, numInputBits, outputBuffer, outputBufferBitOffset, numOutputBits, (b, i) -> b[i], b -> b.length);
    }

    public static <T> void copyBits(T inputBuffer, long inputBufferBitOffset, long numInputBits, byte[] outputBuffer, long outputBufferBitOffset, long numOutputBits,
            ByteGetter<T> byteGetter, ToIntFunction<T> lengthGetter) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("inputBuffer.length", lengthGetter.applyAsInt(inputBuffer)).add("inputBufferBitOffset", inputBufferBitOffset)
                    .add("numInputBits", numInputBits).add("outputBuffer.length", outputBuffer.length).add("outputBufferBitOffset", outputBufferBitOffset)
                    .add("numOutputBits", numOutputBits));
        }

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
            outputBitOffset = outputBufferBitOffset + numZeroOutputBits;
            totalNumBits = numInputBits;
        }
        else {
            inputBitOffset = inputBufferBitOffset;
            outputBitOffset = outputBufferBitOffset;
            totalNumBits = numInputBits;
        }

        if (totalNumBits <= 8) {

            copyLessOrEqualToEightBits(inputBuffer, inputBitOffset, outputBuffer, outputBitOffset, (int)totalNumBits, byteGetter, lengthGetter);
        }
        else {
            copyMoreThanEightBits(inputBuffer, inputBitOffset, outputBuffer, outputBitOffset, totalNumBits, byteGetter, lengthGetter);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    private static <T> void copyLessOrEqualToEightBits(T inputBuffer, long inputBitOffset, byte[] outputBuffer, long outputBitOffset, int totalNumBits,
            ByteGetter<T> byteGetter, ToIntFunction<T> lengthGetter) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("inputBuffer.length", lengthGetter.applyAsInt(inputBuffer)).add("inputBitOffset", inputBitOffset)
                    .add("outputBuffer.length", outputBuffer.length).add("outputBitOffset", outputBitOffset).add("totalNumBits", totalNumBits));
        }

        final int inputBufferByteOffset = (int)(inputBitOffset >>> 3);
        final int outputBufferByteOffset = (int)(outputBitOffset >>> 3);

        final int numInputBits1 = 8 - (int)(inputBitOffset & 0x7L);
        final int numOutputBits1 = 8 - (int)(outputBitOffset & 0x7L);

        final byte inputBits;

        if (numInputBits1 >= totalNumBits) {

            final int shift = numInputBits1 - totalNumBits;

            inputBits = retrieve(byteGetter.get(inputBuffer, inputBufferByteOffset), shift, totalNumBits);
        }
        else {
            final int numInputBits2 = totalNumBits - numInputBits1;

            inputBits = (byte)(
                     (retrieve(byteGetter.get(inputBuffer, inputBufferByteOffset), numInputBits1) << numInputBits2)
                    | retrieve(byteGetter.get(inputBuffer, inputBufferByteOffset + 1), 8 - numInputBits2, numInputBits2));
        }

        if (DEBUG) {

            PrintDebug.debug(debugClass, b -> b.add("totalNumBits", totalNumBits).add("numInputBits1", numInputBits1).add("numOutputBits1", numOutputBits1)
                    .binary("inputBits", inputBits));
        }

        if (numOutputBits1 >= totalNumBits) {

            final byte existingByte = outputBuffer[outputBufferByteOffset];
            final int inputLeftShift = numOutputBits1 - totalNumBits;
            final byte clearMask = (byte)~(BitsUtil.mask(totalNumBits) << inputLeftShift);

            final byte updatedByte = (byte)((existingByte & clearMask) | (inputBits << inputLeftShift));

            outputBuffer[outputBufferByteOffset] = updatedByte;

            if (DEBUG) {

                PrintDebug.debug(debugClass, "update one byte", b -> b.binary("existingByte", existingByte).add("inputLeftShift", inputLeftShift).binary("clearMask", clearMask)
                        .binary("updatedByte", updatedByte));
            }
        }
        else {
            final int numOutputBits2 = totalNumBits - numOutputBits1;

            final byte existingByte1 = outputBuffer[outputBufferByteOffset];
            final byte clearMask1 = BitsUtil.mask(numOutputBits1);

            final byte updatedByte1 = (byte)((existingByte1 & (~clearMask1)) | ((inputBits >>> numOutputBits2) & clearMask1));

            outputBuffer[outputBufferByteOffset] = updatedByte1;

            final int outputBufferByteOffset2 = outputBufferByteOffset + 1;
            final byte existingByte2 = outputBuffer[outputBufferByteOffset2];
            final int inputRightShift2 = 8 - numOutputBits2;
            final byte mask2Shifted = (byte)(BitsUtil.mask(numOutputBits2) << inputRightShift2);
            final byte clearMask2 = (byte)~mask2Shifted;

            final byte updatedByte2 = (byte)((existingByte2 & clearMask2) | ((inputBits << inputRightShift2) & mask2Shifted));

            outputBuffer[outputBufferByteOffset2] = updatedByte2;

            if (DEBUG) {

                PrintDebug.debug(debugClass, "update two bytes", b -> b.binary("existingByte1", existingByte1).binary("clearMask1", clearMask1)
                        .binary("updatedByte1", updatedByte1).add("numOutputBits2", numOutputBits2).add("outputBufferByteOffset2", outputBufferByteOffset2)
                        .add("inputRightShift2", inputRightShift2).binary("mask2Shifted", mask2Shifted).binary("clearMask2", clearMask2).binary("updatedByte2", updatedByte2));
            }
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    private static <T> void copyMoreThanEightBits(T inputBuffer, long startInputBitOffset, byte[] outputBuffer, long startOutputBitOffset, long totalNumBits,
            ByteGetter<T> byteGetter, ToIntFunction<T> lengthGetter) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("inputBuffer.length", lengthGetter.applyAsInt(inputBuffer)).add("startInputBitOffset", startInputBitOffset)
                    .add("outputBuffer.length", outputBuffer.length).add("startInputBitOffset", startInputBitOffset).add("totalNumBits", totalNumBits));
        }

        long inputBitOffset = startInputBitOffset;
        long outputBitOffset = startOutputBitOffset;

        long remainingBits = totalNumBits;

        int inputBufferByteOffset = (int)(inputBitOffset >>> 3);
        int outputBufferByteOffset = (int)(outputBitOffset >>> 3);

        int remainingBitsOfInputByte = 8 - (int)(inputBitOffset & 0x7L);
        int remainingBitsOfOutputByte = 8 - (int)(outputBitOffset & 0x7L);

        do {
            final byte existingByte = outputBuffer[outputBufferByteOffset];

            if (DEBUG) {

                final long r1 = remainingBits;
                final int i = inputBufferByteOffset;
                final int o = outputBufferByteOffset;
                final int r2 = remainingBitsOfInputByte;
                final int r3 = remainingBitsOfOutputByte;

                PrintDebug.debug(debugClass, "start of loop", b -> b.add("inputBitOffset", inputBitOffset).add("outputBitOffset", outputBitOffset)
                        .add("remainingBits", r1).add("inputBufferByteOffset", i).add("outputBufferByteOffset", o)
                        .add("remainingBitsOfInputByte", r2).add("remainingBitsOfOutputByte", r3)
                        .binary("existingByte", existingByte));
            }

            if (remainingBits <= remainingBitsOfInputByte) {

                final int intRemainingBits = (int)remainingBits;

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "remainingBits <= remainingBitsInputByte");
                }

                if (remainingBits <= remainingBitsOfOutputByte) {

                    final int bitsToCopy = intRemainingBits;

                    final int inputRightShift = 8 - bitsToCopy;
                    final byte inputBits = retrieve(byteGetter.get(inputBuffer, inputBufferByteOffset), inputRightShift, bitsToCopy);

                    final int inputLeftShift = remainingBitsOfOutputByte - bitsToCopy;
                    final byte mask = (byte)(BitsUtil.mask(bitsToCopy) << inputLeftShift);

                    final byte updatedByte = (byte)((existingByte & (~mask)) | ((inputBits << inputLeftShift) & mask));

                    outputBuffer[outputBufferByteOffset] = updatedByte;

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "remainingBits <= remainingBitsOfInputByte", b -> b.add("bitsToCopy", bitsToCopy).add("inputRightShift", inputRightShift)
                                .binary("inputBits", inputBits).add("inputLeftShift", inputLeftShift).binary("mask", mask).binary("updatedByte", updatedByte));
                    }
                }
                else {
                    final int bitsToCopy1 = remainingBitsOfOutputByte;

                    final int inputRightShift1 = remainingBitsOfInputByte - bitsToCopy1;
                    final byte inputByte = byteGetter.get(inputBuffer, inputBufferByteOffset);

                    final byte inputBits1 = retrieve(inputByte, inputRightShift1, bitsToCopy1);

                    final int inputLeftShift1 = remainingBitsOfOutputByte - bitsToCopy1;
                    final byte mask1 = (byte)(BitsUtil.mask(bitsToCopy1) << inputLeftShift1);

                    final byte updatedByte1 = (byte)((existingByte & (~mask1)) | ((inputBits1 << inputLeftShift1) & mask1));

                    outputBuffer[outputBufferByteOffset] = updatedByte1;

                    final int bitsToCopy2 = intRemainingBits - remainingBitsOfOutputByte;
                    final int inputRightShift2 = inputRightShift1 - bitsToCopy2;
                    final byte inputBits2 = retrieve(inputByte, inputRightShift2, bitsToCopy2);

                    final int outputBufferByteOffset2 = outputBufferByteOffset + 1;
                    final byte existingByte2 = outputBuffer[outputBufferByteOffset2];
                    final int inputLeftShift2 = 8 - bitsToCopy2;
                    final byte mask2 = (byte)(BitsUtil.mask(bitsToCopy2) << inputLeftShift2);

                    final byte updatedByte2 = (byte)((existingByte2 & (~mask2)) | ((inputBits2 << inputLeftShift2) & mask2));

                    outputBuffer[outputBufferByteOffset2] = updatedByte2;

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "remainingBits > remainingBitsOfOutputByte", b -> b.add("bitsToCopy1", bitsToCopy1)
                                .add("inputRightShift1", inputRightShift1).binary("inputBits1", inputBits1).add("inputLeftShift1", inputLeftShift1).binary("mask1", mask1)
                                .binary("updatedByte1", updatedByte1).add("bitsToCopy2", bitsToCopy2).add("inputRightShift2", inputRightShift2).binary("inputBits2", inputBits2)
                                .add("outputBufferByteOffset2", outputBufferByteOffset2).binary("existingByte2", existingByte2).add("inputLeftShift2", inputLeftShift2)
                                .binary("mask2", mask2).binary("updatedByte2", updatedByte2));
                    }
                }

                remainingBits = 0;
            }
            else {
                if (DEBUG) {

                    PrintDebug.debug(debugClass, "remainingBits > remainingBitsOfInputByte");
                }

                if (remainingBitsOfInputByte < remainingBitsOfOutputByte) {

                    final int bitsToCopy = remainingBitsOfInputByte;

                    final byte inputBits = retrieve(byteGetter.get(inputBuffer, inputBufferByteOffset), bitsToCopy);

                    final int inputLeftShift = remainingBitsOfOutputByte - bitsToCopy;
                    final byte mask = (byte)(BitsUtil.mask(bitsToCopy) << inputLeftShift);

                    final byte updatedByte = (byte)((existingByte & (~mask)) | ((inputBits << inputLeftShift) & mask));

                    outputBuffer[outputBufferByteOffset] = updatedByte;

                    remainingBitsOfInputByte = 8;
                    ++ inputBufferByteOffset;

                    remainingBitsOfOutputByte -= bitsToCopy;
                    remainingBits -= bitsToCopy;

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "remainingBitsOfInputByte < remainingBitsOfOutputByte", b -> b.add("bitsToCopy", bitsToCopy).binary("inputBits", inputBits)
                                .add("inputLeftShift", inputLeftShift).binary("mask", mask).binary("updatedByte", updatedByte));
                    }
                }
                else if (remainingBitsOfInputByte == remainingBitsOfOutputByte) {

                    final int bitsToCopy = remainingBitsOfInputByte;

                    final byte inputBits = retrieve(byteGetter.get(inputBuffer, inputBufferByteOffset), bitsToCopy);

                    final int inputLeftShift = remainingBitsOfOutputByte - bitsToCopy;
                    final byte mask = (byte)(BitsUtil.mask(bitsToCopy) << inputLeftShift);

                    final byte updatedByte = (byte)((existingByte & (~mask)) | ((inputBits << inputLeftShift) & mask));

                    outputBuffer[outputBufferByteOffset] = updatedByte;

                    remainingBitsOfInputByte = 8;
                    ++ inputBufferByteOffset;

                    remainingBitsOfOutputByte = 8;
                    ++ outputBufferByteOffset;
                    remainingBits -= bitsToCopy;

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "remainingBitsOfInputByte == remainingBitsOfOutputByte", b -> b.add("bitsToCopy", bitsToCopy).binary("inputBits", inputBits)
                                .add("inputLeftShift", inputLeftShift).binary("mask", mask).binary("updatedByte", updatedByte));
                    }
                }
                else {
                    final int bitsToCopy = remainingBitsOfOutputByte;

                    final int inputRightShift = remainingBitsOfInputByte - bitsToCopy;

                    final byte inputBits = retrieve(byteGetter.get(inputBuffer, inputBufferByteOffset), inputRightShift, bitsToCopy);

                    final byte mask = BitsUtil.mask(bitsToCopy);

                    final byte updatedByte = (byte)((existingByte & (~mask)) | (inputBits & mask));

                    outputBuffer[outputBufferByteOffset] = updatedByte;

                    remainingBitsOfInputByte -= bitsToCopy;

                    remainingBitsOfOutputByte = 8;
                    ++ outputBufferByteOffset;
                    remainingBits -= bitsToCopy;

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "remainingBitsOfInputByte > remainingBitsOfOutputByte", b -> b.add("bitsToCopy", bitsToCopy)
                                .add("inputRightShift", inputRightShift).binary("inputBits", inputBits)
                                .binary("mask", mask).binary("updatedByte", updatedByte));
                    }
                }
            }

        } while (remainingBits != 0L);

        if (DEBUG) {

            PrintDebug.exit(debugClass);
        }
    }

    public static String toBinaryString(byte[] array) {

        final int length = array.length;

        final StringBuilder sb = new StringBuilder(length * 9 + 1);

        sb.append('[');

        for (int i = 0; i < length; ++ i) {

            if (i > 0) {

                sb.append(',');
            }

            BitsUtil.addByteBinaryString(sb, array[i]);
        }

        sb.append(']');

        return sb.toString();
    }

    private static byte retrieve(byte source, int numBits) {

        return (byte)(source & BitsUtil.mask(numBits));
    }

    private static byte retrieve(byte source, int shift, int numBits) {

        return (byte)((source >>> shift) & BitsUtil.mask(numBits));
    }

    private static void checkBuffer(byte[] buffer, boolean signed, long bufferBitOffset, int numBits, int numBitsToScan) {

        if (numBits > numBitsToScan) {

            throw new IllegalArgumentException();
        }
        else {
            checkBuffer(buffer, signed, bufferBitOffset, numBits);
        }
    }

    private static void checkBuffer(byte[] buffer, boolean signed, long bufferBitOffset, long numBits) {

        if (signed && numBits < 2) {

            throw new IllegalArgumentException();
        }
        else {
            checkBuffer(buffer, bufferBitOffset, numBits);
        }
    }

    private static void checkBuffer(byte[] buffer, long bufferBitOffset, long numBits) {

        if (numBits < 1) {

            throw new IllegalArgumentException();
        }
        else if (numBits > (buffer.length * 8) - bufferBitOffset) {

            throw new IllegalArgumentException();
        }
    }
}
