package dev.jdata.db.utils.adt.numbers;

public interface ILargeNumber {

    int getPrecision();

    public static int countDigits(long value) {

        final int result;

        if (value == 0L) {

            result = 1;
        }
        else {
            int count;

            long v = value;

            for (count = 0; v != 0; ++ count) {

                v /= 10;
            }

            result = count;
        }

        return result;
    }
}
