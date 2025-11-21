package dev.jdata.db.utils.adt.buffers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.arrays.BaseByteLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableOneDimensionalArray;
import dev.jdata.db.utils.adt.numbers.decimals.IMutableDecimal;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.function.ByteGetter;
import dev.jdata.db.utils.math.Sign;
import dev.jdata.db.utils.scalars.Integers;

public final class BitBuffer extends BaseByteLargeArray implements IMutableOneDimensionalArray, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BIT_BUFFER;

    private static final boolean ASSERT = AssertionContants.ASSERT_BIT_BUFFER;

    private final long innerBitsCapacity;
    private final long innerBitsCapacityMask;

    private long bitOffset;

    public BitBuffer(AllocationType allocationType, int innerBytesCapacityExponent) {
        super(allocationType, getInnerBitsCapacityExponent(innerBytesCapacityExponent), true);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("innerBytesCapacityExponent", innerBytesCapacityExponent));
        }

        this.innerBitsCapacity = getInnerElementCapacity();

        final int numBits = innerBytesCapacityExponent + 3;

        this.innerBitsCapacityMask = BitsUtil.maskLong(numBits);

        this.bitOffset = 0L;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public long getCapacity() {

        return getOuterArrayCapacity() << getInnerElementCapacityExponent();
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearArray();

        this.bitOffset = 0L;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public long getLimit() {

        return getBitOffset();
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(isBitSet(index) ? '1' : '0');
    }

    @Override
    public void toHexString(long index, StringBuilder sb) {

        toString(index, sb);
    }

    public long getBitOffset() {
        return bitOffset;
    }

    public boolean getBoolean(long bitOffset) {

        return getLong(bitOffset, false, 1) != 0L;
    }

    public byte getSignedByte(long bitOffset) {

        return (byte)getLong(bitOffset, true, 7);
    }

    public short getUnsignedByte(long bitOffset) {

        return (short)getLong(bitOffset, false, Byte.SIZE);
    }

    public short getSignedShort(long bitOffset) {

        return (short)getLong(bitOffset, true, 15);
    }

    public int getUnsignedShort(long bitOffset) {

        return (int)getLong(bitOffset, false, Short.SIZE);
    }

    public int getSignedInt(long bitOffset) {

        return (int)getLong(bitOffset, true, 31);
    }

    public long getUnsignedInt(long bitOffset) {

        return getLong(bitOffset, false, Integer.SIZE);
    }

    public long getSignedLong(long bitOffset) {

        return getLong(bitOffset, true, 63);
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

        final long innerCapacity = innerBitsCapacity;
        final long innerMask = innerBitsCapacityMask;

        final long afterSignBitOffset;

        final Sign sign;

        if (signed) {

            final int outerIndex = getOuterIndex(bitOffset);
            final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

            final long byteArrayBitOffset = bitOffset & innerMask;

            if (ASSERT) {

                final long numByteArrayBitsRemaining = innerCapacity - byteArrayBitOffset;

                Assertions.isAboveZero(numByteArrayBitsRemaining);
            }

            sign = BitBufferUtil.isBitSet(byteArray, byteArrayBitOffset) ? Sign.MINUS : Sign.PLUS;

            afterSignBitOffset = bitOffset + 1;
        }
        else {
            sign = Sign.PLUS;

            afterSignBitOffset = bitOffset;
        }

        final int outerIndex = getOuterIndex(afterSignBitOffset);
        final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

        final long byteArrayBitOffset = afterSignBitOffset & innerMask;

        final long numByteArrayBitsRemaining = innerCapacity - byteArrayBitOffset;

        final int innerArrayNumElements = getNumInnerElements(outerIndex);

        if (DEBUG) {

            debug("byte array variables", b -> b.add("byteArrayBitOffset", byteArrayBitOffset).add("numByteArrayBitsRemaining", numByteArrayBitsRemaining)
                    .add("innerArrayNumElements", innerArrayNumElements));
        }

        if (innerArrayNumElements < innerBitsCapacity && (byteArrayBitOffset + numBits) > innerArrayNumElements) {

            throw new IllegalArgumentException();
        }

        long unsignedResult;

        if (numBits <= numByteArrayBitsRemaining) {

            unsignedResult = BitBufferUtil.getLongValue(byteArray, false, byteArrayBitOffset, numBits);
        }
        else {
            final int numNextByteArrayBitsToStore = Integers.checkUnsignedLongToUnsignedInt(numBits - numByteArrayBitsRemaining);

            unsignedResult = BitBufferUtil.getLongValue(byteArray, false, byteArrayBitOffset, Integers.checkUnsignedLongToUnsignedInt(numByteArrayBitsRemaining));

            final int nextOuterIndex = getOuterIndex(bitOffset + getInnerElementCapacity());
            final byte[] nextByteArray = getByteArrayByOuterIndex(nextOuterIndex);

            unsignedResult <<= numNextByteArrayBitsToStore;

            if (numNextByteArrayBitsToStore > getNumInnerElements(nextOuterIndex)) {

                throw new IllegalArgumentException();
            }

            unsignedResult |= BitBufferUtil.getLongValue(nextByteArray, false, 0L, numNextByteArrayBitsToStore);
        }

        final long result = sign == Sign.PLUS ? unsignedResult : - unsignedResult;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    public <T extends IMutableDecimal> T getDecimal(long bitOffset, T dst) {

        Checks.isBufferBitsOffset(bitOffset);
        Objects.requireNonNull(dst);

        throw new UnsupportedOperationException();
    }

    public void addSignedByte(byte value) {

        addLong(value, true, 7);
    }

    public void addUnsignedByte(short value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        if (value < 0) {

            throw new IllegalArgumentException();
        }
        else if (value >= 1 << Byte.SIZE) {

            throw new IllegalArgumentException();
        }

        addLong(value, false, Byte.SIZE);

        if (DEBUG) {

            exit();
        }
    }

    public void addSignedShort(short value) {

        addLong(value, true, 15);
    }

    public void addUnsignedShort(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        if (value < 0) {

            throw new IllegalArgumentException();
        }
        else if (value >= 1 << Short.SIZE) {

            throw new IllegalArgumentException();
        }

        addLong(value, false, Short.SIZE);

        if (DEBUG) {

            exit();
        }
    }

    public void addSignedInt(int value) {

        addLong(value, true, 31);
    }

    public void addUnsignedInt(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        if (value < 0) {

            throw new IllegalArgumentException();
        }
        else if (value >= 1L << Integer.SIZE) {

            throw new IllegalArgumentException();
        }

        addLong(value, false, Integer.SIZE);

        if (DEBUG) {

            exit();
        }
    }

    public void addSignedLong(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addLong(value, true, 63);

        if (DEBUG) {

            exit();
        }
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

        if (DEBUG) {

            debug("instance variables", b -> b.add("bitOffset", bitOffset));
        }

        final long innerCapacity = innerBitsCapacity;

        final long unsignedValue;
        final int numSignAdditionalBits;

        if (signed) {

            if (value == Long.MIN_VALUE) {

                throw new IllegalArgumentException();
            }

            final long limit = bitOffset ++;

            int outerIndex = getLastByteArrayIndexOrAllocateForOneAppendedElement(limit);

            int numByteArrayStoredBits = getNumInnerElements(outerIndex);
/*
            if (ASSERT) {

                final long remainingBits = innerCapacity - numByteArrayStoredBits;

                Assertions.isAboveZero(remainingBits);
            }
*/
            if (numByteArrayStoredBits == innerCapacity) {

                outerIndex = checkCapacityWithNewLimitAndReturnOuterIndex(limit, limit + numBits, shouldClear());

                numByteArrayStoredBits = 0;
            }

            final int signBitOffset = numByteArrayStoredBits;

            final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

            BitBufferUtil.setBitValue(byteArray, value < 0L, signBitOffset);

            if (DEBUG) {

                final int numBytes = BitBufferUtil.numBytes(numByteArrayStoredBits + 1);

                debug("set sign bit", b -> b.add("signBitOffset", signBitOffset).add("byteArray", byteArrayString(byteArray, numBytes)));
            }

            unsignedValue = value < 0L ? - value : value;

            numSignAdditionalBits = 1;
        }
        else {
            unsignedValue = value;
            numSignAdditionalBits = 0;
        }

        final long limit = bitOffset;

        int outerIndex = getLastByteArrayIndexOrAllocateForOneAppendedElement(limit);

        int numByteArrayStoredBits = getNumInnerElements(outerIndex) + numSignAdditionalBits;

        if (numByteArrayStoredBits == innerCapacity) {

            outerIndex = checkCapacityWithNewLimitAndReturnOuterIndex(limit, limit + numBits, shouldClear());

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

        final long numByteArrayBitsRemaining = innerCapacity - numByteArrayStoredBits;

        if (numBits <= numByteArrayBitsRemaining) {

            BitBufferUtil.setLongValue(byteArray, unsignedValue, false, dstBitOffset, numBits);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numBits;

            if (DEBUG) {

                debugByteArray("updated one out of one byte arrays", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumInnerElements(outerIndex, updatedNumByteArrayStoredBits);
        }
        else {
            final int bitsRemaining = Integers.checkUnsignedLongToUnsignedInt(numByteArrayBitsRemaining);

            final int numNextByteArrayBitsToStore = numBits - bitsRemaining;

            BitBufferUtil.setLongValue(byteArray, unsignedValue >>> numNextByteArrayBitsToStore, false, dstBitOffset, bitsRemaining);

            final long updatedNumByteArrayStoredBits = numByteArrayStoredBits + numByteArrayBitsRemaining;

            if (DEBUG) {

                debugByteArray("updated one out of two byte arrays", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumInnerElements(outerIndex, updatedNumByteArrayStoredBits);

            final int nextOuterIndex = getOuterIndex(limit + bitsRemaining);

            ensureCapacityAndLimitWithNewLimit(limit, limit + numBits, shouldClear());

            final byte[] nextByteArray = getByteArrayByOuterIndex(nextOuterIndex);

            BitBufferUtil.setLongValue(nextByteArray, unsignedValue & BitsUtil.maskLong(numNextByteArrayBitsToStore), false, 0L, numNextByteArrayBitsToStore);

            if (DEBUG) {

                debugByteArray("updated two out of two byte arrays", nextByteArray, numNextByteArrayBitsToStore);
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

        final long limit = bitOffset;

        final int outerIndex = getLastByteArrayIndexOrAllocateForOneAppendedElement(limit);
        final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

        final int numByteArrayStoredBits = getNumInnerElements(outerIndex);

        final long dstBitOffset = numByteArrayStoredBits;

        final long numValueBitsRemaining = innerBitsCapacity - numByteArrayStoredBits;

        long srcBitOffset = byteBufferBitOffset;

        if (numBits <= numValueBitsRemaining) {

            BitBufferUtil.copyBits(byteBuffer, srcBitOffset, numBits, byteArray, dstBitOffset, numBits, byteGetter, lengthGetter);

            final int updatedNumByteArrayStoredBits = numByteArrayStoredBits + numBits;

            if (DEBUG) {

                debugByteArray("updated one out of one byte arrays", byteArray, updatedNumByteArrayStoredBits);
            }

            setNumInnerElements(outerIndex, numByteArrayStoredBits + numBits);
        }
        else {
            if (numValueBitsRemaining > 0L) {

                BitBufferUtil.copyBits(byteBuffer, srcBitOffset, numValueBitsRemaining, byteArray, dstBitOffset, numValueBitsRemaining, byteGetter, lengthGetter);

                final long updatedNumByteArrayStoredBits = numByteArrayStoredBits + numValueBitsRemaining;

                if (DEBUG) {

                    debugByteArray("updated one out of two byte arrays", byteArray, updatedNumByteArrayStoredBits);
                }

                setNumInnerElements(outerIndex, updatedNumByteArrayStoredBits);
            }

            final int bitsRemaining = Integers.checkUnsignedLongToUnsignedInt(numValueBitsRemaining);

            final int numNextByteArrayBitsToStore = numBits - bitsRemaining;

            final int nextOuterIndex = getOuterIndex(limit + bitsRemaining);

            ensureCapacityAndLimitWithNewLimit(limit, limit + numBits, shouldClear());

            final byte[] nextByteArray = getByteArrayByOuterIndex(nextOuterIndex);

            BitBufferUtil.copyBits(byteBuffer, srcBitOffset + numValueBitsRemaining, numNextByteArrayBitsToStore, nextByteArray, 0L, numNextByteArrayBitsToStore, byteGetter,
                    lengthGetter);

            if (DEBUG) {

                debugByteArray("updated two out of two byte arrays", byteArray, numNextByteArrayBitsToStore);
            }

            setNumInnerElements(outerIndex + 1, numNextByteArrayBitsToStore);
        }

        this.bitOffset += numBits;

        if (DEBUG) {

            exit(b -> b.add("this.bitOffset", bitOffset));
        }
    }

    @Override
    protected void clearInnerArray(byte[] innerArray, long startIndex, long numElements) {

        assertShouldClear();

        final int byteStartIndex = Integers.checkUnsignedLongToUnsignedInt(BitBufferUtil.byteOffsetExact(startIndex));
        final int numBytes = Integers.checkUnsignedLongToUnsignedInt(BitBufferUtil.numBytesExact(numElements));

        Arrays.fill(innerArray, byteStartIndex, byteStartIndex + numBytes, (byte)0);
    }

    @Override
    protected int getInnerByteArrayLength(long innerArrayElementCapacity) {

        return Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity >>> 3);
    }

    private boolean isBitSet(long bitOffset) {

        final int outerIndex = getOuterIndex(bitOffset);
        final byte[] byteArray = getByteArrayByOuterIndex(outerIndex);

        final long byteArrayBitOffset = bitOffset & innerBitsCapacityMask;

        return BitBufferUtil.isBitSet(byteArray, byteArrayBitOffset);
    }

    private static int getInnerBitsCapacityExponent(int innerBytesCapacityExponent) {

        return innerBytesCapacityExponent + 3;
    }

    private void debugByteArray(String message, byte[] byteArray, long numByteArrayStoredBits) {

        final byte[] closureByteArray = byteArray;
        final long closureNumByteArrayStoredBits = numByteArrayStoredBits;

        final int numToPrint = BitBufferUtil.numBytes(numByteArrayStoredBits);

        debug(message, b -> b.add("byteArray", byteArrayString(closureByteArray, numToPrint)).add("numByteArrayStoredBits", closureNumByteArrayStoredBits));
    }

    private static String byteArrayString(byte[] byteArray, int numToPrint) {

        return Array.toString(byteArray, 0, Math.min(numToPrint, 1000));
    }
}
