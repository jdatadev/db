package dev.jdata.db.utils.adt.buffers;

import java.nio.ByteBuffer;
import java.util.function.ToIntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.arrays.BaseLargeByteArray;
import dev.jdata.db.utils.adt.decimals.MutableDecimal;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.function.ByteGetter;
import dev.jdata.db.utils.scalars.Integers;

public final class BitBuffer extends BaseLargeByteArray implements PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BIT_BUFFER;

    private final int innerBitsCapacity;
    private final long innerBitsCapacityMask;

    private long bitOffset;

    public BitBuffer(int innerBytesCapacityExponent) {
        super(innerBytesCapacityExponent, true);

        if (DEBUG) {

            enter(b -> b.add("innerBytesCapacityExponent", innerBytesCapacityExponent));
        }

        this.innerBitsCapacity = getInnerCapacity() * Byte.SIZE;

        final int numBits = innerBytesCapacityExponent + 3;

        this.innerBitsCapacityMask = BitsUtil.maskLong(numBits);

        this.bitOffset = 0L;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        super.clear();

        this.bitOffset = 0L;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void toString(long index, StringBuilder sb) {

    }

    public long getBitOffset() {
        return bitOffset;
    }

    public boolean getBoolean(long bitOffset) {

        return getLong(bitOffset, false, 1) != 0L;
    }

    public int getUnsignedByte(long bitOffset) {

        return (byte)getLong(bitOffset, false, Byte.SIZE);
    }

    public short getSignedShort(long bitOffset) {

        return (short)getLong(bitOffset, true, Short.SIZE);
    }

    public int getUnsignedShort(long bitOffset) {

        return (int)getLong(bitOffset, false, Short.SIZE);
    }

    public int getSignedInt(long bitOffset) {

        return (int)getLong(bitOffset, true, Integer.SIZE);
    }

    public long getUnsignedInt(long bitOffset) {

        return getLong(bitOffset, false, Integer.SIZE);
    }

    public long getSignedLong(long bitOffset) {

        return getLong(bitOffset, true, Long.SIZE);
    }

    public long getUnsignedLong(long bitOffset) {

        return getLong(bitOffset, false, Long.SIZE);
    }

    public float getFloat(long bitOffset) {

        return Float.intBitsToFloat((int)getLong(bitOffset, false, Float.SIZE));
    }

    public double getDouble(long bitOffset) {

        return Double.longBitsToDouble(getLong(bitOffset, false, Double.SIZE));
    }

    private long getLong(long bitOffset, boolean signed, int numBits) {

        Checks.isBufferBitsOffset(bitOffset);
        Checks.isLongNumBits(numBits, signed);
        Checks.isLessThanOrEqualTo(bitOffset, this.bitOffset - numBits);

        if (DEBUG) {

            enter(b -> b.add("bitOffset", bitOffset).add("signed", signed).add("numBits", numBits));
        }

        final long byteOffset = bitOffset >>> 3;

        final int outerIndex = getOuterIndex(byteOffset);
        final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

        final long byteArrayBitOffset = bitOffset & innerBitsCapacityMask;

        final long numByteArrayBitsRemaining = innerBitsCapacity - byteArrayBitOffset;

        final int innerArrayNumElements = getNumInnerElements(outerIndex);

        if (DEBUG) {

            debug("byte array variables", b -> b.add("byteOffset", byteOffset).add("byteArrayBitOffset", byteArrayBitOffset)
                    .add("numByteArrayBitsRemaining", numByteArrayBitsRemaining).add("innerArrayNumElements", innerArrayNumElements));
        }

        if (innerArrayNumElements < innerBitsCapacity && (byteArrayBitOffset + numBits) > innerArrayNumElements) {

            throw new IllegalArgumentException();
        }

        long result;

        if (numBits <= numByteArrayBitsRemaining) {

            result = BitBufferUtil.getLongValue(byteArray, signed, byteArrayBitOffset, numBits);
        }
        else {
            final int numNextByteArrayBitsToStore = Integers.checkUnsignedLongToUnsignedInt(numBits - numByteArrayBitsRemaining);

            result = BitBufferUtil.getLongValue(byteArray, signed, byteArrayBitOffset, Integers.checkUnsignedLongToUnsignedInt(numByteArrayBitsRemaining));

            final int nextOuterIndex = getOuterIndex(byteOffset + getInnerCapacity());
            final byte[] nextByteArray = getByteArrayByOuterIndex(nextOuterIndex);

            result <<= numNextByteArrayBitsToStore;

            if (numNextByteArrayBitsToStore > getNumInnerElements(nextOuterIndex)) {

                throw new IllegalArgumentException();
            }

            result |= BitBufferUtil.getLongValue(nextByteArray, false, 0L, numNextByteArrayBitsToStore);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    public final MutableDecimal getDecimal(long bitOffset, MutableDecimal dst) {

        throw new UnsupportedOperationException();
    }

    public void addUnsignedShort(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        if (value < 0) {

            throw new IllegalArgumentException();
        }
        else if (value > Short.MAX_VALUE) {

            throw new IllegalArgumentException();
        }

        addLong(value, false, Short.SIZE);

        if (DEBUG) {

            exit();
        }
    }

    private void addShort(int value, boolean signed, int numBits) {

        Checks.isShortNumBits(numBits, signed);

        addLong(value, signed, numBits);
    }

    private void addInt(int value, boolean signed, int numBits) {

        Checks.isIntNumBits(numBits, signed);

        addLong(value, signed, numBits);
    }

    public void addUnsignedLong(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addLong(value, false, Long.SIZE);

        if (DEBUG) {

            exit();
        }
    }

    private void addLong(long value, boolean signed, int numBits) {

        Checks.isLongNumBits(numBits, signed);

        if (DEBUG) {

            enter(b -> b.add("value", value).add("signed", signed).add("numBits", numBits));
        }

        if (signed) {

            throw new UnsupportedOperationException();
        }

        int outerIndex = getLastByteArrayIndexOrAllocateForOneAppendedElement();

        int numByteArrayStoredBits = getNumInnerElements(outerIndex);

        if (numByteArrayStoredBits == innerBitsCapacity) {

            outerIndex = checkCapacityAndReturnOuterIndex(numBits);

            numByteArrayStoredBits = 0;
        }

        final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

        if (DEBUG) {

            final byte[] closureByteArray = byteArray;
            final int closureNumByteArrayStoredBits = numByteArrayStoredBits;

            final int numToPrint = BitBufferUtil.numBytes(numByteArrayStoredBits);

            debug("byte array", b -> b.add("byteArray", byteArrayString(closureByteArray, numToPrint)).add("numByteArrayStoredBits", closureNumByteArrayStoredBits));
        }

        final long dstBitOffset = numByteArrayStoredBits;

        final int numByteArrayBitsRemaining = innerBitsCapacity - numByteArrayStoredBits;

        if (numBits <= numByteArrayBitsRemaining) {

            BitBufferUtil.setLongValue(byteArray, value, signed, dstBitOffset, numBits);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numBits;

            if (DEBUG) {

                debugByteArray("updated one out of one byte array", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumInnerElements(outerIndex, updatedNumByteArrayStoredBits);
        }
        else {
            final int numNextByteArrayBitsToStore = numBits - numByteArrayBitsRemaining;

            BitBufferUtil.setLongValue(byteArray, value >>> numNextByteArrayBitsToStore, signed, dstBitOffset, numByteArrayBitsRemaining);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numByteArrayBitsRemaining;

            if (DEBUG) {

                debugByteArray("updated one out of two byte array", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumInnerElements(outerIndex, updatedNumByteArrayStoredBits);

            final byte[] nextByteArray = checkCapacity(numNextByteArrayBitsToStore);

            BitBufferUtil.setLongValue(nextByteArray, value & BitsUtil.maskLong(numNextByteArrayBitsToStore), signed, 0L, numNextByteArrayBitsToStore);

            if (DEBUG) {

                debugByteArray("updated two out of two byte array", nextByteArray, numNextByteArrayBitsToStore);
            }

            setNumInnerElements(outerIndex + 1, numNextByteArrayBitsToStore);
        }

        this.bitOffset += numBits;

        if (DEBUG) {

            exit(b -> b.add("this.bitOffset", bitOffset));
        }
    }

    public void addBytes(byte[] byteBuffer, long byteBufferBitOffset, int numBits) {

        addBytes(byteBuffer, byteBufferBitOffset, numBits, (b, i) -> b[i], b -> b.length);
    }

    public void addBytes(ByteBuffer byteBuffer, long byteBufferBitOffset, int numBits) {

        addBytes(byteBuffer, byteBufferBitOffset, numBits, ByteBuffer::get, ByteBuffer::remaining);
    }

    private <T> void addBytes(T byteBuffer, long byteBufferBitOffset, int numBits, ByteGetter<T> byteGetter, ToIntFunction<T> lengthGetter) {

        Checks.isNumBits(numBits);
        Checks.isLessThanOrEqualTo(numBits, innerBitsCapacity);

        if (DEBUG) {

            enter(b -> b.add("byteBuffer.length", lengthGetter.applyAsInt(byteBuffer)).add("byteBufferBitOffset", byteBufferBitOffset).add("numBits", numBits));
        }

        final int outerIndex = getLastByteArrayIndexOrAllocateForOneAppendedElement();
        final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

        final int numByteArrayStoredBits = getNumInnerElements(outerIndex);

        final long dstBitOffset = numByteArrayStoredBits;

        final int numValueBitsRemaining = innerBitsCapacity - numByteArrayStoredBits;

        long srcBitOffset = byteBufferBitOffset;

        if (numBits <= numValueBitsRemaining) {

            BitBufferUtil.copyBits(byteBuffer, srcBitOffset, numBits, byteArray, dstBitOffset, numBits, byteGetter, lengthGetter);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numBits;

            if (DEBUG) {

                debugByteArray("updated one out of one byte array", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumInnerElements(outerIndex, numByteArrayStoredBits + numBits);
        }
        else {
            if (numValueBitsRemaining > 0) {

                BitBufferUtil.copyBits(byteBuffer, srcBitOffset, numValueBitsRemaining, byteArray, dstBitOffset, numValueBitsRemaining, byteGetter, lengthGetter);

                final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numValueBitsRemaining;

                if (DEBUG) {

                    debugByteArray("updated one out of two byte array", byteArray, updatedNumByteArrayStoredBits);
                }

                setNumInnerElements(outerIndex, updatedNumByteArrayStoredBits);
            }

            final int numNextByteArrayBitsToStore = numBits - numValueBitsRemaining;
            final byte[] nextByteArray = checkCapacity(numNextByteArrayBitsToStore);

            BitBufferUtil.copyBits(byteBuffer, srcBitOffset + numValueBitsRemaining, numNextByteArrayBitsToStore, nextByteArray, 0L, numNextByteArrayBitsToStore, byteGetter,
                    lengthGetter);

            if (DEBUG) {

                debugByteArray("updated two out of two byte array", byteArray, numNextByteArrayBitsToStore);
            }

            setNumInnerElements(outerIndex + 1, numNextByteArrayBitsToStore);
        }

        this.bitOffset += numBits;

        if (DEBUG) {

            exit(b -> b.add("this.bitOffset", bitOffset));
        }
    }

    @Override
    protected long getInnerElementCapacity() {

        return innerBitsCapacity;
    }

    private void debugByteArray(String message, byte[] byteArray, int numByteArrayStoredBits) {

        final byte[] closureByteArray = byteArray;
        final int closureNumByteArrayStoredBits = numByteArrayStoredBits;

        final int numToPrint = BitBufferUtil.numBytes(numByteArrayStoredBits);

        debug(message, b -> b.add("byteArray", byteArrayString(closureByteArray, numToPrint)).add("numByteArrayStoredBits", closureNumByteArrayStoredBits));
    }

    private static String byteArrayString(byte[] byteArray, int numToPrint) {

        return Array.toString(byteArray, 0, Math.min(numToPrint, 1000));
    }
}
