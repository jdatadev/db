package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.LongToIntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.elements.ByIndex.IByIndexElementEqualityTester;
import dev.jdata.db.utils.adt.elements.ByIndex.IntByIndexStringAdder;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiObjToIntFunction;
import dev.jdata.db.utils.function.IntToByteFunction;
import dev.jdata.db.utils.function.IntToIntFunction;
import dev.jdata.db.utils.scalars.Integers;

public class Array {

    public static <T, P> long count(T[] array, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(array);
        Objects.requireNonNull(predicate);

        long count = 0;

        for (T element : array) {

            if (predicate.test(element, parameter)) {

                ++ count;
            }
        }

        return count;
    }

    public static <T> long closureOrConstantCount(T[] array, Predicate<T> predicate) {

        Objects.requireNonNull(array);
        Objects.requireNonNull(predicate);

        return count(array, predicate, (e, p) -> p.test(e));
    }

    public static int sum(int[] array) {

        Checks.isNotEmpty(array);

        int sum = 0;

        for (int element : array) {

            sum += element;
        }

        return sum;
    }

    public static <T> int sum(T[] array, ToIntFunction<T> mapper) {

        Checks.isNotEmpty(array);
        Objects.requireNonNull(mapper);

        int sum = 0;

        for (T element : array) {

            sum += mapper.applyAsInt(element);
        }

        return sum;
    }

    public static <T> int maxInt(T[] array, int defaultValue, ToIntFunction<T> mapper) {

        Objects.requireNonNull(array);
        Objects.requireNonNull(mapper);

        int max = Integer.MIN_VALUE;
        boolean found = false;

        for (T element : array) {

            final int value = mapper.applyAsInt(element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    public static <T> long maxLong(T[] array, long defaultValue, ToLongFunction<T> mapper) {

        Objects.requireNonNull(array);
        Objects.requireNonNull(mapper);

        long max = Long.MIN_VALUE;
        boolean found = false;

        for (T element : array) {

            final long value = mapper.applyAsLong(element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    public static <T> boolean containsInstance(T[] array, T instance) {

        return ByIndex.containsInstance(array, array.length, instance, (b, i) -> b[(int)i], ArrayIndexOutOfBoundsException::new);
    }

    public static <T> boolean containsInstance(T[] array, T instance, int startIndex, int numElements) {

        return ByIndex.containsInstance(array, array.length, instance, startIndex, numElements, (b, i) -> b[(int)i], ArrayIndexOutOfBoundsException::new);
    }

    public static <T, P> boolean contains(T[] array, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.contains(array, array.length, parameter, (b, i) -> b[(int)i], predicate, ArrayIndexOutOfBoundsException::new);
    }

    public static <T> boolean closureOrConstantContains(T[] array, Predicate<T> predicate) {

        return ByIndex.contains(array, array.length, predicate, (b, i) -> b[(int)i], (e, p) -> p.test(e), ArrayIndexOutOfBoundsException::new);
    }

    public static <T, P> T findAtMostOne(T[] array, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(array);
        Objects.requireNonNull(predicate);

        return findAtMostOne(array, 0, array.length, parameter, predicate);
    }

    public static <T, P> T findAtMostOne(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(array);
        Checks.checkFromIndexSize(startIndex, numElements, array.length);
        Objects.requireNonNull(predicate);

        T result = null;
        boolean hasFoundElement = false;

        for (int i = 0; i < numElements; ++ i) {

            final T element = array[i];

            if (predicate.test(element, parameter)) {

                if (hasFoundElement) {

                    throw new IllegalStateException();
                }

                result = element;
                break;
            }
        }

        return result;
    }

    public static <T, P> T findExactlyOne(T[] array, P parameter, BiPredicate<T, P> predicate) {

        return findExactlyOne(array, 0, array.length, parameter, predicate);
    }

    public static <T, P> T findExactlyOne(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        T result = null;
        boolean hasFoundElement = false;

        for (int i = 0; i < numElements; ++ i) {

            final T element = array[startIndex + i];

            if (predicate.test(element, parameter)) {

                if (hasFoundElement) {

                    throw new IllegalStateException();
                }

                result = element;
                break;
            }
        }

        if (!hasFoundElement) {

            throw new NoSuchElementException();
        }

        return result;
    }

    public static <T, P> int findAtMostOneIndex(T[] array, P parameter, BiPredicate<T, P> predicate) {

        return (int)ByIndex.findAtMostOneIndex(array, array.length, parameter, (b, i) -> b[(int)i], predicate, ArrayIndexOutOfBoundsException::new);
    }

    public static <T> int closureOrConstantFindAtMostOneIndex(T[] array, Predicate<T> predicate) {

        return (int)ByIndex.findAtMostOneIndex(array, array.length, predicate, (b, i) -> b[(int)i], (e, p) -> p.test(e), ArrayIndexOutOfBoundsException::new);
    }

    public static <T, P> int findAtMostOneIndex(T[] array, P parameter, int startIndex, int numElements, BiPredicate<T, P> predicate) {

        return (int)ByIndex.findAtMostOneIndex(array, array.length, startIndex, numElements, parameter, (b, i) -> b[(int)i], predicate, ArrayIndexOutOfBoundsException::new);
    }

    public static <T> int closureOrConstantFindAtMostOneIndex(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return (int)ByIndex.findAtMostOneIndex(array, array.length, startIndex, numElements, predicate, (b, i) -> b[(int)i], predicate != null ? (e, p) -> p.test(e) : null,
                ArrayIndexOutOfBoundsException::new);
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

    public static byte[] copyOf(byte[] toCopy) {

        Objects.requireNonNull(toCopy);

        return Arrays.copyOf(toCopy, toCopy.length);
    }

    public static int[] copyOf(int[] toCopy) {

        Objects.requireNonNull(toCopy);

        return Arrays.copyOf(toCopy, toCopy.length);
    }

    public static long[] copyOf(long[] toCopy) {

        Objects.requireNonNull(toCopy);

        return Arrays.copyOf(toCopy, toCopy.length);
    }

    public static long[] safeCopyOf(long[] toCopy) {

        return toCopy != null ? Arrays.copyOf(toCopy, toCopy.length) : null;
    }

    public static <T> T[] copyOf(T[] toCopy) {

        Objects.requireNonNull(toCopy);

        return Arrays.copyOf(toCopy, toCopy.length);
    }

    @FunctionalInterface
    public interface IntMapper<P> {

        int apply(int element, P parameter);
    }

    public static <P> int[] mapInt(int[] array, P parameter, IntMapper<P> mapper) {

        Objects.requireNonNull(array);
        Objects.requireNonNull(mapper);

        final int arrayLength = array.length;

        final int[] result = new int[arrayLength];

        for (int i = 0; i < arrayLength; ++ i) {

            result[i] = mapper.apply(array[i], parameter);
        }

        return result;
    }

    public static <P> int[] closureOrConstantMapInt(int[] array, IntToIntFunction mapper) {

        return mapInt(array, mapper, (e, p) -> p.apply(e));
    }

    public static <T> int[] closureOrConstantMapToInt(T[] toMap, ToIntFunction<T> mapper) {

        Objects.requireNonNull(toMap);
        Objects.requireNonNull(mapper);

        return mapToInt(toMap, mapper, (e, p) -> p.applyAsInt(e));
    }

    public static <T, P> int[] mapToInt(T[] toMap, P parameter, BiObjToIntFunction<T, P> mapper) {

        Objects.requireNonNull(toMap);
        Objects.requireNonNull(mapper);

        final int arrayLength = toMap.length;

        final int[] mapped = new int[arrayLength];

        for (int i = 0; i < arrayLength; ++ i) {

            mapped[i] = mapper.apply(toMap[i], parameter);
        }

        return mapped;
    }

    @FunctionalInterface
    public interface IMapByIndexToIntFunction<T, P> {

        int apply(T byIndex, int index, P parameter);
    }

    public static <T, P> int[] mapToInt(T toMap, int toMapLength, P parameter, IMapByIndexToIntFunction<T, P> mapper) {

        Objects.requireNonNull(toMap);
        Checks.isLengthAboveOrAtZero(toMapLength);
        Objects.requireNonNull(mapper);

        final int[] mapped = new int[toMapLength];

        for (int i = 0; i < toMapLength; ++ i) {

            mapped[i] = mapper.apply(toMap, i, parameter);
        }

        return mapped;
    }

    public static <T, R> R[] map(T[] toMap, IntFunction<R[]> createMappedArray, Function<T, R> mapper) {

        Objects.requireNonNull(toMap);
        Objects.requireNonNull(createMappedArray);
        Objects.requireNonNull(mapper);

        final int arrayLength = toMap.length;

        final R[] mapped = createMappedArray.apply(arrayLength);

        ByIndex.map(toMap, arrayLength, (a, i) -> a[(int)i], mapper, (a, i, e) -> a[(int)i] = e, mapped);

        return mapped;
    }

    public static byte[] toUnsignedByteArray(int[] values) {

        Objects.requireNonNull(values);

        return toByteArray(values, Integers::checkUnsignedIntToUnsignedByteAsByte);
    }

    private static byte[] toByteArray(int[] values, IntToByteFunction intToByteFunction) {

        final int length = values.length;

        final byte[] result = new byte[length];

        for (int i = 0; i < length; ++ i) {

            result[i] = intToByteFunction.apply(values[i]);
        }

        return result;
    }

    public static int[] checkToIntArray(long[] values) {

        return toIntArray(values, Integers::checkLongToInt);
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

    public static long[] toLongArray(int[] values) {

        final int length = values.length;

        final long[] result = new long[length];

        for (int i = 0; i < length; ++ i) {

            result[i] = values[i];
        }

        return result;
    }

    public static void move(long[] array, int startIndex, int numElements, int delta) {

        Objects.requireNonNull(array);
        Checks.isLengthAboveZero(numElements);
        Checks.isNotZero(delta);

        System.arraycopy(array, startIndex, array, startIndex + delta, numElements);
    }

    public static <T> boolean equals(T[] array1, int startIndex1, T[] array2, int startIndex2, int numElements) {

        Objects.requireNonNull(array1);
        Checks.checkFromIndexSize(startIndex1, numElements, array1.length);
        Objects.requireNonNull(array2);
        Checks.checkFromIndexSize(startIndex2, numElements, array2.length);

        boolean equals = true;

        for (int i = 0; i < numElements; ++ i) {

            if (!Objects.equals(array1[startIndex1 + i], array2[startIndex2 + i])) {

                equals = false;
                break;
            }
        }

        return equals;
    }

    @FunctionalInterface
    public interface IArrayEqualityTester<T, P1, P2> extends IByIndexElementEqualityTester<T, P1, P2> {

    }

    public static <T, P1, P2> boolean equals(T[] array1, P1 parameter1, T[] array2, P2 parameter2, IArrayEqualityTester<T, P1, P2> equalityTester) {

        final boolean result;

        final int array1Length = array1.length;

        if (array1Length == array2.length) {

            result = equals(array1, 0, parameter1, array2, 0, parameter2, array1Length, equalityTester);
        }
        else {
            result = false;
        }

        return result;
    }

    public static <T, P1, P2> boolean equals(T[] array1, int startIndex1, P1 parameter1, T[] array2, int startIndex2, P2 parameter2, int numElements,
            IArrayEqualityTester<T, P1, P2> equalityTester) {

        return ByIndex.equals(array1, startIndex1, parameter1, array2, startIndex2, parameter2, numElements, equalityTester,
                (a1, i1, p1, a2, i2, p2, predicate) -> predicate.equals(a1[(int)i1], p1, a2[(int)i2], p2));
    }

    public static String toString(byte[] array, int startIndex, int numElements) {

        Objects.requireNonNull(array);
        Checks.checkFromToIndex(startIndex, numElements, array.length);

        return ByIndex.closureOrConstantToString(array, startIndex, numElements, null, (a, i, b) -> b.append(a[(int)i]));
    }

    public static String toString(int[] array, int startIndex, int numElements) {

        Objects.requireNonNull(array);
        Checks.checkFromToIndex(startIndex, numElements, array.length);

        return ByIndex.closureOrConstantToString(array, startIndex, numElements, null, (a, i, b) -> b.append(a[(int)i]));
    }

    public static String toString(int[] array, int startIndex, int numElements, IntPredicate predicate) {

        Objects.requireNonNull(array);
        Checks.checkFromToIndex(startIndex, numElements, array.length);
        Objects.requireNonNull(predicate);

        return ByIndex.closureOrConstantToString(array, startIndex, numElements, null, (a, i) -> predicate.test(a[(int)i]), (a, i, b) -> b.append(a[(int)i]));
    }

    public static void toString(int[] array, int startIndex, int numElements, StringBuilder sb, IntPredicate predicate) {

        Objects.requireNonNull(array);
        Checks.checkFromToIndex(startIndex, numElements, array.length);
        Objects.requireNonNull(sb);
        Objects.requireNonNull(predicate);

        ByIndex.closureOrConstantToString(array, startIndex, numElements, sb, null, (a, i) -> predicate.test(a[(int)i]), (a, i, b) -> b.append(a[(int)i]));
    }

    public static void toString(int[] array, int startIndex, int numElements, StringBuilder sb, IntPredicate predicate, IntByIndexStringAdder<int[]> byIndexStringAdder) {

        Objects.requireNonNull(array);
        Checks.checkFromToIndex(startIndex, numElements, array.length);
        Objects.requireNonNull(sb);
        Objects.requireNonNull(predicate);

        ByIndex.closureOrConstantToString(array, startIndex, numElements, sb, null, (a, i) -> predicate.test(a[(int)i]),
                (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    public static String toString(long[] array, int startIndex, int numElements, LongPredicate predicate) {

        Objects.requireNonNull(array);
        Checks.checkFromToIndex(startIndex, numElements, array.length);
        Objects.requireNonNull(predicate);

        return ByIndex.closureOrConstantToString(array, startIndex, numElements, null, (a, i) -> predicate.test(a[(int)i]), (a, i, b) -> b.append(a[(int)i]));
    }

    public static void toString(long[] array, int startIndex, int numElements, StringBuilder sb, LongPredicate predicate) {

        Objects.requireNonNull(array);
        Checks.checkFromToIndex(startIndex, numElements, array.length);
        Objects.requireNonNull(sb);
        Objects.requireNonNull(predicate);

        ByIndex.closureOrConstantToString(array, startIndex, numElements, sb, null, (a, i) -> predicate.test(a[(int)i]), (a, i, b) -> b.append(a[(int)i]));
    }
}
