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
import dev.jdata.db.utils.function.ByteGetter;
import dev.jdata.db.utils.scalars.Integers;

public final class BitBuffer extends BaseLargeByteArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_BIT_BUFFER;

    private static final int NUM_INNER_ELEMENTS_NUM_BITS = NUM_INNER_ELEMENTS_NUM_BYTES * Byte.SIZE;

    private final int innerBitsCapacity;
    private final long innerBitsCapacityMask;

    private long bitOffset;

    public BitBuffer(int innerBytesCapacityExponent) {
        super(innerBytesCapacityExponent);

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
    protected void toString(long index, StringBuilder sb) {

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

        final byte[] byteArray = getBuffer(byteOffset);

        final long byteArrayStoredBitOffset = bitOffset & innerBitsCapacityMask;
        final long byteArrayBitOffset = byteArrayStoredBitOffset + NUM_INNER_ELEMENTS_NUM_BITS;

        final long numByteArrayBitsRemaining = innerBitsCapacity - byteArrayStoredBitOffset;

        final int innerArrayNumElements = getInnerArrayNumElements(byteArray);

        if (DEBUG) {

            debug("byte array variables", b -> b.add("byteOffset", byteOffset).add("byteArrayStoredBitOffset", byteArrayStoredBitOffset)
                    .add("byteArrayBitOffset", byteArrayBitOffset).add("numByteArrayBitsRemaining", numByteArrayBitsRemaining)
                    .add("innerArrayNumElements", innerArrayNumElements));
        }

        if (innerArrayNumElements < innerBitsCapacity && (byteArrayStoredBitOffset + numBits) > innerArrayNumElements) {

            throw new IllegalArgumentException();
        }

        long result;

        if (numBits <= numByteArrayBitsRemaining) {

            result = BitBufferUtil.getLongValue(byteArray, signed, byteArrayBitOffset, numBits);
        }
        else {
            final int numNextByteArrayBitsToStore = Integers.checkUnsignedLongToUnsignedInt(numBits - numByteArrayBitsRemaining);

            result = BitBufferUtil.getLongValue(byteArray, signed, byteArrayBitOffset, Integers.checkUnsignedLongToUnsignedInt(numByteArrayBitsRemaining));

            final byte[] nextByteArray = getBuffer(byteOffset + getInnerCapacity());

            result <<= numNextByteArrayBitsToStore;

            if (numNextByteArrayBitsToStore > getInnerArrayNumElements(nextByteArray)) {

                throw new IllegalArgumentException();
            }

            result |= BitBufferUtil.getLongValue(nextByteArray, false, NUM_INNER_ELEMENTS_NUM_BITS, numNextByteArrayBitsToStore);
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

        byte[] byteArray = getLastBufferOrAllocate();

        int numByteArrayStoredBits = getInnerArrayNumElements(byteArray);

        if (numByteArrayStoredBits == innerBitsCapacity) {

            byteArray = allocate();

            numByteArrayStoredBits = 0;
        }

        if (DEBUG) {

            final byte[] closureByteArray = byteArray;
            final int closureNumByteArrayStoredBits = numByteArrayStoredBits;

            final int numToPrint = BitBufferUtil.numBytes(numByteArrayStoredBits) + NUM_INNER_ELEMENTS_NUM_BYTES;

            debug("byte array", b -> b.add("byteArray", byteArrayString(closureByteArray, numToPrint)).add("numByteArrayStoredBits", closureNumByteArrayStoredBits));
        }

        final long dstBitOffset = numByteArrayStoredBits + NUM_INNER_ELEMENTS_NUM_BITS;

        final int numByteArrayBitsRemaining = innerBitsCapacity - numByteArrayStoredBits;

        if (numBits <= numByteArrayBitsRemaining) {

            BitBufferUtil.setLongValue(byteArray, value, signed, dstBitOffset, numBits);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numBits;

            if (DEBUG) {

                debugByteArray("updated one out of one byte array", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumElements(byteArray, updatedNumByteArrayStoredBits);
        }
        else {
            final int numNextByteArrayBitsToStore = numBits - numByteArrayBitsRemaining;

            BitBufferUtil.setLongValue(byteArray, value >>> numNextByteArrayBitsToStore, signed, dstBitOffset, numByteArrayBitsRemaining);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numByteArrayBitsRemaining;

            if (DEBUG) {

                debugByteArray("updated one out of two byte array", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumElements(byteArray, updatedNumByteArrayStoredBits);

            final byte[] nextByteArray = allocate();

            BitBufferUtil.setLongValue(nextByteArray, value & BitsUtil.maskLong(numNextByteArrayBitsToStore), signed, NUM_INNER_ELEMENTS_NUM_BITS, numNextByteArrayBitsToStore);

            if (DEBUG) {

                debugByteArray("updated two out of two byte array", byteArray, numNextByteArrayBitsToStore);
            }

            setNumElements(nextByteArray, numNextByteArrayBitsToStore);
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

        Checks.isLessThanOrEqualTo(numBits, innerBitsCapacity);

        if (DEBUG) {

            enter(b -> b.add("byteBuffer.length", lengthGetter.applyAsInt(byteBuffer)).add("byteBufferBitOffset", byteBufferBitOffset).add("numBits", numBits));
        }

        final byte[] byteArray = getLastBufferOrAllocate();

        final int numByteArrayStoredBits = getInnerArrayNumElements(byteArray);

        final long dstBitOffset = numByteArrayStoredBits + NUM_INNER_ELEMENTS_NUM_BITS;

        final int numValueBitsRemaining = innerBitsCapacity - numByteArrayStoredBits;

        long srcBitOffset = byteBufferBitOffset;

        if (numBits < numValueBitsRemaining) {

            BitBufferUtil.copyBits(byteBuffer, srcBitOffset, numBits, byteArray, dstBitOffset, numBits, byteGetter, lengthGetter);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numBits;

            if (DEBUG) {

                debugByteArray("updated one out of one byte array", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumElements(byteArray, numByteArrayStoredBits + numBits);
        }
        else {
            final int numNextByteArrayBitsToStore = numBits - numValueBitsRemaining;

            BitBufferUtil.copyBits(byteBuffer, srcBitOffset, numValueBitsRemaining, byteArray, dstBitOffset, numValueBitsRemaining, byteGetter, lengthGetter);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numValueBitsRemaining;

            if (DEBUG) {

                debugByteArray("updated one out of two byte array", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumElements(byteArray, updatedNumByteArrayStoredBits);

            final byte[] nextByteArray = allocate();

            BitBufferUtil.copyBits(byteBuffer, srcBitOffset + numValueBitsRemaining, numNextByteArrayBitsToStore, nextByteArray, NUM_INNER_ELEMENTS_NUM_BITS,
                    numNextByteArrayBitsToStore, byteGetter, lengthGetter);

            if (DEBUG) {

                debugByteArray("updated two out of two byte array", byteArray, numNextByteArrayBitsToStore);
            }

            setNumElements(nextByteArray, numNextByteArrayBitsToStore);
        }

        this.bitOffset += numBits;

        exit(b -> b.add("this.bitOffset", bitOffset));
    }

    private void debugByteArray(String message, byte[] byteArray, int numByteArrayStoredBits) {

        final byte[] closureByteArray = byteArray;
        final int closureNumByteArrayStoredBits = numByteArrayStoredBits;

        final int numToPrint = BitBufferUtil.numBytes(numByteArrayStoredBits) + NUM_INNER_ELEMENTS_NUM_BYTES;

        debug(message, b -> b.add("byteArray", byteArrayString(closureByteArray, numToPrint)).add("numByteArrayStoredBits", closureNumByteArrayStoredBits));
    }

    private static String byteArrayString(byte[] byteArray, int numToPrint) {

        return Array.toString(byteArray, 0, Math.min(numToPrint, 1000));
    }
}
