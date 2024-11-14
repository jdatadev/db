package dev.jdata.db.utils.bits;

public class BitsUtil {

    public static byte mask(int numBits) {

        return (byte)((1 << numBits) - 1);
    }

    public static byte merge(byte byte1, byte byte2, int numBitsFromByte1) {

        final byte mask = mask(8 - numBitsFromByte1);

        return (byte)((byte1 & (~mask)) | (byte2 & mask));
    }

    public static int getNumBits(int value, boolean signed) {

        return getNumBits(value < 0 ? (value & 0x7FFFFFFFL) | 0x80000000L : value, 32, signed);
    }

    public static int getNumBits(long value, boolean signed) {

        return getNumBits(value, 64, signed);
    }

    private static int getNumBits(long value, int numBitsToScan, boolean signed) {

System.out.format("get num bits 0x%016x %d %b\n", value, numBitsToScan, signed);

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

        return numBits;
//        return numBits + (signed ? 1 : 2);
    }
}
