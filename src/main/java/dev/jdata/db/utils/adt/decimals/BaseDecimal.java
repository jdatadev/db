package dev.jdata.db.utils.adt.decimals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

import dev.jdata.db.utils.adt.buffers.BitBuffer;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseDecimal<T extends BaseDecimal<T>> implements IDecimal, Comparable<T> {

    private static final Boolean DEBUG = false;

    private static final Class<?> debugClass = BaseDecimal.class;

    private byte[] bits;
    private int precision;
    private int scale;
    private int numBitsBeforeDecimalPoint;
    private int totalNumBits;

    BaseDecimal() {

    }

    BaseDecimal(int precision, int scale) {

        this.precision = Checks.isDecimalPrecision(precision);
        this.scale = scale;

        throw new UnsupportedOperationException();
    }

    final void initialize(long l) {

    }

    final void initialize(BigDecimal bigDecimal) {

        Objects.requireNonNull(bigDecimal);

        final int scale = bigDecimal.scale();

        Checks.isNotNegative(scale);

        this.precision = bigDecimal.precision();
        this.scale = scale;

        final boolean isNegative = bigDecimal.signum() == -1;

        final BigDecimal bigDecimalValue = isNegative ? bigDecimal.negate() : bigDecimal;


        final BigInteger integer = bigDecimalValue.toBigInteger();

        final BigDecimal decimals = bigDecimalValue.subtract(bigDecimalValue.setScale(0, RoundingMode.DOWN));
        final BigInteger decimalsUnscaled = decimals.unscaledValue();

        final int integerNumBits = integer.bitLength();
        final int decimalsNumBits = decimalsUnscaled.bitLength();

        this.numBitsBeforeDecimalPoint = integerNumBits;
        this.totalNumBits = integerNumBits + decimalsNumBits;

        final int numBytes = BitBufferUtil.numBytes(1 + totalNumBits);

        this.bits = new byte[numBytes];

        BitBufferUtil.setBitValue(bits, isNegative, 0L);

        int dstBitOffset = 1;

        if (DEBUG) {

            PrintDebug.debug(debugClass, "constructor " + integer + ' ' + integerNumBits + ' ' + decimalsUnscaled + ' ' + decimalsNumBits);
        }

        for (int i = integerNumBits - 1; i >= 0; -- i) {

            BitBufferUtil.setBitValue(bits, integer.testBit(i), dstBitOffset ++);
        }

        for (int i = decimalsNumBits - 1; i >= 0; -- i) {

            BitBufferUtil.setBitValue(bits, decimalsUnscaled.testBit(i), dstBitOffset ++);
        }
    }

    @Override
    public final int getPrecision() {

        return precision;
    }

    final void initializeDecimal(BitBuffer buffer, long bufferBitOffset, int precision, int scale) {

        Objects.requireNonNull(buffer);
        Checks.isBufferBitsOffset(bufferBitOffset);
        Checks.isAboveZero(precision);
        Checks.isNotNegative(scale);

        this.precision = precision;
        this.scale = scale;

        throw new UnsupportedOperationException();
    }

    BigDecimal getValue() {

        final boolean isNegative = BitBufferUtil.isBitSet(bits, 0L);

        final BigInteger integerPart = getBits(1, numBitsBeforeDecimalPoint);

        final int numBitsAfterDecimalPoint = totalNumBits - numBitsBeforeDecimalPoint;

        final BigInteger decimalPart = getBits(1 + numBitsBeforeDecimalPoint, numBitsAfterDecimalPoint);

        if (DEBUG) {

            PrintDebug.debug(debugClass, "get value " + integerPart + ' ' + numBitsBeforeDecimalPoint + ' ' + decimalPart + ' ' + numBitsAfterDecimalPoint);
        }

        final BigDecimal integerDecimal = new BigDecimal(integerPart);

        final BigDecimal bigDecimal = numBitsAfterDecimalPoint != 0
                ? integerDecimal.add(new BigDecimal(decimalPart).movePointLeft(scale))
                : integerDecimal;

        return isNegative ? bigDecimal.negate() : bigDecimal;
    }

    private BigInteger getBits(int startBitOffset, int numBits) {

        Checks.isBufferBitsOffset(startBitOffset);
        Checks.isNumBits(numBits);

        if (DEBUG) {

            PrintDebug.debug(debugClass, "get bits " + startBitOffset + ' ' + numBits);
        }

        final int numBytes = BitBufferUtil.numBytes(numBits);

        final byte[] byteArray = new byte[numBytes];

        int dstBitOffset = (numBytes << 3) - numBits;

        for (int i = 0; i < numBits; ++ i) {

            final boolean isSet = BitBufferUtil.isBitSet(bits, startBitOffset + i);

            if (DEBUG) {

                PrintDebug.debug(debugClass, "num bytes " + numBytes + ' ' + i + ' ' + (startBitOffset + i) + ' ' + isSet + ' ' + dstBitOffset);
            }

            BitBufferUtil.setBitValue(byteArray, isSet, dstBitOffset ++);
        }

        return new BigInteger(byteArray);
    }

    private boolean isNegative() {

        return BitBufferUtil.isBitSet(bits, 0L);
    }

    final int compareToOther(BaseDecimal<T> other) {

        final int result;

        if (numBitsBeforeDecimalPoint < other.numBitsBeforeDecimalPoint) {

            result = -1;
        }
        else if (numBitsBeforeDecimalPoint == other.numBitsBeforeDecimalPoint) {

            final boolean isNegative = isNegative();
            final boolean isOtherNegative = other.isNegative();

            if (isNegative == isOtherNegative) {

                int compareResult = compareBits(bits, other.bits, 1, numBitsBeforeDecimalPoint);

                if (compareResult == 0) {

                    result = compareAfterDecimalPoint(other, numBitsBeforeDecimalPoint);
                }
                else {
                    result = compareResult;
                }
            }
            else if (isNegative && !isOtherNegative) {

                result = -1;
            }
            else if (!isNegative && isOtherNegative) {

                result = 1;
            }
            else {
                throw new IllegalStateException();
            }
        }
        else {
            result = 1;
        }

        return result;
    }

    private int compareAfterDecimalPoint(BaseDecimal<T> other, int numBitsBeforeDecimalPoint) {

        final int numBitsAfterDecimalPoint = totalNumBits - numBitsBeforeDecimalPoint;
        final int otherNumBitsAfterDecimalPoint = other.totalNumBits - numBitsBeforeDecimalPoint;

        final int numCommonBitsAfterDecimalPoint = Math.min(numBitsAfterDecimalPoint, otherNumBitsAfterDecimalPoint);

        final int signAndNumBitsBeforeDecimalPoint = 1 + numBitsBeforeDecimalPoint;

        int compareResult = compareBits(bits, other.bits, signAndNumBitsBeforeDecimalPoint, numCommonBitsAfterDecimalPoint);

        final int result;

        if (compareResult == 0) {

            if (numBitsAfterDecimalPoint < otherNumBitsAfterDecimalPoint) {

                final int otherNumAdditionalBits = otherNumBitsAfterDecimalPoint - numBitsAfterDecimalPoint;

                final int startBitOffset = signAndNumBitsBeforeDecimalPoint + numCommonBitsAfterDecimalPoint;

                for (int i = 0; i < otherNumAdditionalBits; ++ i) {

                    if (BitBufferUtil.isBitSet(other.bits, startBitOffset + i)) {

                        compareResult = -1;
                        break;
                    }
                }

                result = compareResult;
            }
            else if (numBitsAfterDecimalPoint == otherNumBitsAfterDecimalPoint) {

                result = 0;
            }
            else {
                final int otherNumAdditionalBits = otherNumBitsAfterDecimalPoint - numBitsAfterDecimalPoint;

                final int startBitOffset = signAndNumBitsBeforeDecimalPoint + numCommonBitsAfterDecimalPoint;

                for (int i = 0; i < otherNumAdditionalBits; ++ i) {

                    if (BitBufferUtil.isBitSet(other.bits, startBitOffset + i)) {

                        compareResult = -1;
                        break;
                    }
                }

                result = compareResult;
            }
        }
        else {
            result = compareResult;
        }

        return result;
    }

    private static int compareBits(byte[] bits, byte[] otherBits, int startOffset, int numBits) {

        int compareResult = 0;

        for (int i = 0; i < numBits; ++ i) {

            final int bitOffset = startOffset + i;

            final boolean isSet = BitBufferUtil.isBitSet(bits, bitOffset);
            final boolean isOtherSet = BitBufferUtil.isBitSet(otherBits, bitOffset);

            if (!isSet && isOtherSet) {

                compareResult = -1;
                break;
            }
            else if (isSet && !isOtherSet) {

                compareResult = 1;
                break;
            }
        }

        return compareResult;
    }

    final void clearImpl() {

        this.numBitsBeforeDecimalPoint = -1;
        this.totalNumBits = -1;
    }
}
