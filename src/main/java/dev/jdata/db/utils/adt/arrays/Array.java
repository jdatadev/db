package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.IntResultFunction;

public class Array {

    public static int sum(int[] array) {

        Checks.isNotEmpty(array);

        int sum = 0;

        for (int element : array) {

            sum += element;
        }

        return sum;
    }

    public static <T> int sum(T[] array, IntResultFunction<T> mapper) {

        Checks.isNotEmpty(array);
        Objects.requireNonNull(mapper);

        int sum = 0;

        for (T element : array) {

            sum += mapper.apply(element);
        }

        return sum;
    }

    public static <T> int max(T[] array, int defaultValue, IntResultFunction<T> mapper) {

        Objects.requireNonNull(array);
        Objects.requireNonNull(mapper);

        int max = Integer.MIN_VALUE;
        boolean found = false;

        for (T element : array) {

            final int value = mapper.apply(element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }
}
