package dev.jdata.db.utils.bits;

import java.util.Objects;

import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public class BitsUtil {

    private static final boolean DEBUG = Boolean.FALSE;

    private static final Class<?> debugClass = BitsUtil.class;

    public static byte mask(int numBits) {

        return (byte)((1 << numBits) - 1);
    }

    public static int maskInt(int numBits, int shift) {

        return ((1 << numBits) - 1) << shift;
    }

    public static long maskLong(int numBits) {

        return (1L << numBits) - 1;
    }

    public static long maskLong(int numBits, int shift) {

        return ((1L << numBits) - 1) << shift;
    }

    public static long applyShiftedMask(long value, long shiftedMask, int shift) {

        return (value & shiftedMask) >>> shift;
    }

    public static long setWithShiftedMask(long existingValue, long value, long shiftedMask, int shift) {

        return (existingValue & (~shiftedMask)) | (value << shift);
    }

    public static byte merge(byte byte1, byte byte2, int numBitsFromByte1) {

        final byte mask = mask(8 - numBitsFromByte1);

        return (byte)((byte1 & (~mask)) | (byte2 & mask));
    }

    public static long getMaxLong(int numBits) {

        Checks.isWithinRangeUpperExclusive(numBits, 1, Long.SIZE);

        return (1L << numBits) - 1L;
    }

    public static int getNumEnumBits(Class<? extends Enum<?>> enumClass) {

        Objects.requireNonNull(enumClass);

        return getNumUnsignedBits(enumClass.getEnumConstants().length - 1);
    }

    public static int getNumUnsignedBits(int value) {

        Checks.isNotNegative(value);

        return getNumStorageBits(value, false);
    }

    public static int getNumStorageBits(int value, boolean signed) {

        return getNumStorageBits(value < 0 ? (value & 0x7FFFFFFFL) | 0x80000000L : value, signed, 32);
    }

    public static int getNumStorageBits(long value, boolean signed) {

        return getNumStorageBits(value, signed, 64);
    }

    static int getNumStorageBits(long value, boolean signed, int numBitsToScan) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.format("value", "0x%016x", value).format("signed", "%b", signed).add("numBitsToScan", numBitsToScan));
        }

        int numBits;

        if (value == 0L) {

            numBits = signed ? 2 : 1;
        }
        else {
            numBits = 0;

            final int numBitsToScanMinusSign = signed ? numBitsToScan - 1 : numBitsToScan;

            for (int i = 0; i < numBitsToScanMinusSign; ++ i) {

                if ((value & (1L << i)) != 0L) {

                    numBits = i;
                }
            }
/*
            if (!signed) {

                ++ numBits;
            }
*/
            numBits += signed ? 2 : 1;
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, numBits);
        }

        return numBits;
//        return numBits + (signed ? 1 : 2);
    }

    public static long getRange(long value, int highBit, int numBits) {

        if (AssertionContants.ASSERT_BITS_UTIL_GET_RANGE) {

            if (highBit > 63) {

                throw new IllegalArgumentException();
            }

            Checks.isNotNegative(highBit);

            if (numBits > highBit + 1) {

                throw new IllegalArgumentException();
            }

            Checks.isNumBits(numBits);
        }

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.binary("value", value).add("highBit", highBit).add("numBits", numBits));
        }

        final long mask = ((1L << numBits) - 1);

        final int maskShift = highBit - numBits + 1;

        final long result = (value & (mask << maskShift)) >>> maskShift;

        if (DEBUG) {

            PrintDebug.exit(debugClass, PrintDebug.binaryString(result));
        }

        return result;
    }

    public static int getIndexOfHighestSetBit(int value) {

        return getIndexOfHighestSetBit(value, 32) ;
    }

    public static int getIndexOfHighestSetBit(long value) {

        return getIndexOfHighestSetBit(value, 64);
    }

    private static int getIndexOfHighestSetBit(long value, int numBitsToScan) {

        int index = -1;

        for (int i = 0; i < numBitsToScan; ++ i) {

            if ((value & (1L << i)) != 0L) {

                index = i;
            }
        }

        return index;
    }

    public static final char BINARY_PREFIX = 'b';

    public static String binaryString(byte value) {

        return binaryString(value, false);
    }

    public static String binaryString(byte value, boolean addPrefix) {

        final StringBuilder sb = new StringBuilder(8);

        addByteBinaryString(sb, value, addPrefix);

        return sb.toString();
    }

    public static void addByteBinaryString(StringBuilder sb, byte b) {

        addByteBinaryString(sb, b, false);
    }

    public static void addByteBinaryString(StringBuilder sb, byte b, boolean addPrefix) {

        Objects.requireNonNull(sb);

        if (addPrefix) {

            sb.append(BINARY_PREFIX);
        }

        for (int i = 7; i >= 0; -- i) {

            sb.append((b & (1 << i)) != 0 ? '1' : '0');
        }
    }
}
