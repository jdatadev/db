package dev.jdata.db.utils.adt.numbers;

import dev.jdata.db.utils.adt.IView;

public interface ILargeNumberView extends IView, ILargeNumberGetters {

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
