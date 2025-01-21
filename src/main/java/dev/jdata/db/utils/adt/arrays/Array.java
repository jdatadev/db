package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.LongToIntFunction;
import java.util.function.Predicate;

import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.IntResultFunction;
import dev.jdata.db.utils.function.IntToByteFunction;
import dev.jdata.db.utils.scalars.Integers;

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

    public static <T> boolean containsInstance(T[] array, T instance) {

        return ByIndex.containsInstance(array, array.length, instance, (b, i) -> b[i], ArrayIndexOutOfBoundsException::new);
    }

    public static <T> boolean containsInstance(T[] array, int startIndex, int numElements, T instance) {

        return ByIndex.containsInstance(array, array.length, startIndex, numElements, instance, (b, i) -> b[i], ArrayIndexOutOfBoundsException::new);
    }

    public static <T> int findIndex(T[] array, Predicate<T> predicate) {

        return ByIndex.findIndex(array, array.length, (b, i) -> predicate.test(b[i]), ArrayIndexOutOfBoundsException::new);
    }

    public static <T> int findIndex(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return ByIndex.findIndex(array, array.length, startIndex, numElements, (b, i) -> predicate.test(b[i]), ArrayIndexOutOfBoundsException::new);
    }

    public static Integer[] boxed(int[] toBox) {

        Objects.requireNonNull(toBox);

        final int numElements = toBox.length;

        final Integer[] result = new Integer[numElements];

        for (int i = 0; i < numElements; ++ i) {

            result[i] = toBox[i];
        }

        return result;
    }

    public static int[] copyOf(int[] toCopy) {

        Objects.requireNonNull(toCopy);

        return Arrays.copyOf(toCopy, toCopy.length);
    }

    public static long[] copyOf(long[] toCopy) {

        Objects.requireNonNull(toCopy);

        return Arrays.copyOf(toCopy, toCopy.length);
    }

    public static <T> T[] copyOf(T[] toCopy) {

        Objects.requireNonNull(toCopy);

        return Arrays.copyOf(toCopy, toCopy.length);
    }

    public static byte[] toUnsignedByteArray(int[] values) {

        Objects.requireNonNull(values);

        return toByteArray(values, Integers::checkUnsignedIntToUnsignedByte);
    }

    private static byte[] toByteArray(int[] values, IntToByteFunction intToByteFunction) {

        final int length = values.length;

        final byte[] result = new byte[length];

        for (int i = 0; i < length; ++ i) {

            result[i] = intToByteFunction.apply(values[i]);
        }

        return result;
    }

    public static int[] checkToUnsignedIntArray(long[] values) {

        return toIntArray(values, Integers::checkUnsignedLongToUnsignedInt);
    }

    private static int[] toIntArray(long[] values, LongToIntFunction longToIntFunction) {

        final int length = values.length;

        final int[] result = new int[length];

        for (int i = 0; i < length; ++ i) {

            result[i] = longToIntFunction.applyAsInt(values[i]);
        }

        return result;
    }

    static long[] toLongArray(int[] values) {

        final int length = values.length;

        final long[] result = new long[length];

        for (int i = 0; i < length; ++ i) {

            result[i] = values[i];
        }

        return result;
    }
}
