package dev.jdata.db.utils.adt.arrays;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.BaseByIndexTest;
import dev.jdata.db.utils.scalars.Integers;

public final class ArrayByIndexTest extends BaseByIndexTest {

    public ArrayByIndexTest() {
        super(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testSumInts() {

        assertThatThrownBy(() -> Array.sum(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.sum(new int[0])).isInstanceOf(IllegalArgumentException.class);

        assertThat(Array.sum(new int[] { 0 })).isEqualTo(0);
        assertThat(Array.sum(new int[] { 0, 1 })).isEqualTo(1);
        assertThat(Array.sum(new int[] { 0, 1, 2 })).isEqualTo(3);
        assertThat(Array.sum(new int[] { 0, 1, -2 })).isEqualTo(-1);
    }

    @Test
    @Category(UnitTest.class)
    public void testSumObjects() {

        assertThatThrownBy(() -> Array.sum(null, Integer::intValue)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.sum(new Integer[] { 1, 2, 3 }, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.sum(new Integer[0], Integer::intValue)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Array.sum(new Integer[] { 0 },           Integer::intValue)).isEqualTo(0);
        assertThat(Array.sum(new Integer[] { 0, 1 },        Integer::intValue)).isEqualTo(1);
        assertThat(Array.sum(new Integer[] { 0, 1, 2 },     Integer::intValue)).isEqualTo(3);
        assertThat(Array.sum(new Integer[] { 0, 1, -2 },    Integer::intValue)).isEqualTo(-1);
    }

    @Test
    @Category(UnitTest.class)
    public void testMaxInt() {

        checkMax(Array::maxInt);
    }

    @Test
    @Category(UnitTest.class)
    public void testMaxLong() {

        checkMax((a, d, m) -> Integers.checkLongToInt(Array.maxLong(a, d, m != null ? e -> e.intValue() : null)));
    }

    @FunctionalInterface
    private interface MaxFunction {

        int apply(Integer[] elements, int defaultValue, ToIntFunction<Integer> mapper);
    }

    private static void checkMax(MaxFunction maxFunction) {

        assertThatThrownBy(() -> maxFunction.apply(null, 0, Integer::intValue)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> maxFunction.apply(new Integer[] { 1, 2, 3 }, 0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> maxFunction.apply(new Integer[] { 1, 2, 3 }, 0, null)).isInstanceOf(NullPointerException.class);

        assertThat(maxFunction.apply(new Integer[0], 0, Integer::intValue)).isEqualTo(0);
        assertThat(maxFunction.apply(new Integer[0], 123, Integer::intValue)).isEqualTo(123);

        assertThat(maxFunction.apply(new Integer[] { 0 },               123, Integer::intValue)).isEqualTo(0);
        assertThat(maxFunction.apply(new Integer[] { 0, 1 },            123, Integer::intValue)).isEqualTo(1);
        assertThat(maxFunction.apply(new Integer[] { 1, 2 ,0 },         123, Integer::intValue)).isEqualTo(2);
        assertThat(maxFunction.apply(new Integer[] { 1, 0, -2 },        123, Integer::intValue)).isEqualTo(1);
        assertThat(maxFunction.apply(new Integer[] { 1, 0, 1, -2 },     123, Integer::intValue)).isEqualTo(1);
        assertThat(maxFunction.apply(new Integer[] { -1, 0, -1, -2 },   123, Integer::intValue)).isEqualTo(0);
        assertThat(maxFunction.apply(new Integer[] { -1, -3, -1, -2 },  123, Integer::intValue)).isEqualTo(-1);
    }

    @Test
    @Category(UnitTest.class)
    public void testBoxedIntArray() {

        checkCopyElements(Function.identity(), Array::boxed, (a, i) -> a[i], a -> a.length);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyOfByteArray() {

        checkCopyElements(a -> Array.mapInt(a, null, (e, p) -> Integers.checkIntToByte(e)), Array::copyOf, (a, i) -> a[i], a -> a.length);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyOfIntArray() {

        checkCopyElements(Function.identity(), Array::copyOf, (a, i) -> a[i], a -> a.length);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyOfLongArray() {

        checkCopyOfElementsByIndex(a -> Array.toLongArray(a), Array::copyOf, (a, i) -> a[i], a -> a.length, (e, i) -> e == i);
    }

    @Test
    @Category(UnitTest.class)
    public void testSafeCopyOfLongArray() {

        checkSafeCopyOfElementsByIndex(a -> Array.toLongArray(a), Array::safeCopyOf, (a, i) -> a[i], a -> a.length, (e, i) -> e == i);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyOfElementsArray() {

        checkCopyOfElementsByIndex(Array::boxed, Array::copyOf, (a, i) -> a[i], a -> a.length, (e, i) -> e == i);
    }

    @Test
    @Category(UnitTest.class)
    public void testMapInt() {

        final Object parameter = new Object();

        assertThatThrownBy(() -> Array.mapInt(null, parameter, (e, p) -> 0)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.mapInt(create(123), parameter, null)).isInstanceOf(NullPointerException.class);

        assertThat(Array.mapInt(create(123), parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return e + 111;
        }))
        .containsExactly(234);

        final int[] oneMapped = Array.mapInt(create(123), parameter, (e, p) -> e + 111);
        assertThat(oneMapped).containsExactly(234);

        final int[] threeMapped = Array.mapInt(create(1, 2, 3), parameter, (e, p) -> e + 10);
        assertThat(threeMapped).containsExactly(11, 12, 13);
    }

    @Test
    @Category(UnitTest.class)
    public void testClosureOrConstantMapInt() {

        assertThatThrownBy(() -> Array.closureOrConstantMapInt(null, e -> 0)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.closureOrConstantMapInt(create(123), null)).isInstanceOf(NullPointerException.class);

        final int[] oneMapped = Array.closureOrConstantMapInt(create(123), i -> i + 111);
        assertThat(oneMapped).containsExactly(234);

        final int[] threeMapped = Array.closureOrConstantMapInt(create(1, 2, 3), i -> i + 10);
        assertThat(threeMapped).containsExactly(11, 12, 13);
    }

    @Test
    @Category(UnitTest.class)
    public void testClosureOrConstantMapToInt() {

        assertThatThrownBy(() -> Array.closureOrConstantMapToInt(null, e -> 0)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.closureOrConstantMapToInt(create("abc"), null)).isInstanceOf(NullPointerException.class);

        final int[] oneMapped = Array.closureOrConstantMapToInt(create("1"), Integer::parseInt);
        assertThat(oneMapped).containsExactly(1);

        final int[] threeMapped = Array.closureOrConstantMapToInt(create("1", "2", "3"), Integer::parseInt);
        assertThat(threeMapped).containsExactly(1, 2, 3);
    }

    @Test
    @Category(UnitTest.class)
    public void testMapToInt() {

        final Object parameter = new Object();

        assertThatThrownBy(() -> Array.mapToInt(null, parameter, (e, p) -> 0)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.mapToInt(create("abc"), parameter, null)).isInstanceOf(NullPointerException.class);

        assertThat(Array.mapToInt(create("123"), parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return Integer.parseInt(e);
        }))
        .containsExactly(123);

        final int[] oneMapped = Array.mapToInt(create("1"), parameter, (e, p) -> Integer.parseInt(e));
        assertThat(oneMapped).containsExactly(1);

        final int[] threeMapped = Array.mapToInt(create("1", "2", "3"), parameter, (e, p) -> Integer.parseInt(e));
        assertThat(threeMapped).containsExactly(1, 2, 3);
    }

    @Test
    @Category(UnitTest.class)
    public void testMapByIndexToInt() {

        final Object parameter = new Object();

        assertThatThrownBy(() -> Array.mapToInt(null, 0, parameter, (b, i, p) -> 0)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.mapToInt(create("abc"), -1, parameter, (b, i, p) -> 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Array.mapToInt(create("abc"), parameter, null)).isInstanceOf(NullPointerException.class);

        assertThat(Array.mapToInt(create("123"), 1, parameter, (b, i, p) -> {

            assertThat(p).isSameAs(parameter);

            return Integer.parseInt(b[i]);
        }))
        .containsExactly(123);

        final int[] oneMapped = Array.mapToInt(create("1"), 1, parameter, (b, i, p) -> Integer.parseInt(b[i]));
        assertThat(oneMapped).containsExactly(1);

        final int[] threeMapped = Array.mapToInt(create("1", "2", "3"), 3, parameter, (b, i, p) -> Integer.parseInt(b[i]));
        assertThat(threeMapped).containsExactly(1, 2, 3);

        final int[] twoOutOfThreeMapped = Array.mapToInt(create("1", "2", "3"), 2, parameter, (b, i, p) -> Integer.parseInt(b[i]));
        assertThat(twoOutOfThreeMapped).containsExactly(1, 2);
    }

    @Test
    @Category(UnitTest.class)
    public void testToUnsignedByteArray() {

        assertThatThrownBy(() -> Array.toUnsignedByteArray(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.toUnsignedByteArray(new int[] { -1 })).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Array.toUnsignedByteArray(new int[] { 256 })).isInstanceOf(IllegalArgumentException.class);

        assertThat(Array.toUnsignedByteArray(new int[0])).isEmpty();
        assertThat(Array.toUnsignedByteArray(new int[] { 0 })).containsExactly((byte)0);
        assertThat(Array.toUnsignedByteArray(new int[] { 255 })).containsExactly((byte)255);
        assertThat(Array.toUnsignedByteArray(new int[] { 0 })).containsExactly((byte)0);
        assertThat(Array.toUnsignedByteArray(new int[] { 1, 0 })).containsExactly((byte)1, (byte)0);
        assertThat(Array.toUnsignedByteArray(new int[] { 1, 0, 2 })).containsExactly((byte)1, (byte)0, (byte)2);
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckToIntArray() {

        final long minUnsignedInt = Integer.MIN_VALUE;
        final long maxUnsignedInt = Integer.MAX_VALUE;

        assertThatThrownBy(() -> Array.checkToUnsignedIntArray(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.checkToUnsignedIntArray(new long[] { minUnsignedInt - 1L })).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Array.checkToUnsignedIntArray(new long[] { maxUnsignedInt + 1L })).isInstanceOf(IllegalArgumentException.class);

        assertThat(Array.checkToIntArray(new long[0])).isEmpty();
        assertThat(Array.checkToIntArray(new long[] { 0L })).containsExactly(0);

        assertThat(Array.checkToIntArray(new long[] { minUnsignedInt })).containsExactly((int)(minUnsignedInt));
        assertThat(Array.checkToIntArray(new long[] { maxUnsignedInt })).containsExactly((int)(maxUnsignedInt));
        assertThat(Array.checkToIntArray(new long[] { 0L })).containsExactly(0);
        assertThat(Array.checkToIntArray(new long[] { 1L, 0L })).containsExactly(1, 0);
        assertThat(Array.checkToIntArray(new long[] { 1L, 0L, 2L })).containsExactly(1, 0, 2);
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckToUnsignedIntArray() {

        final long maxUnsignedInt = Integer.MAX_VALUE;

        assertThatThrownBy(() -> Array.checkToUnsignedIntArray(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.checkToUnsignedIntArray(new long[] { -1L })).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Array.checkToUnsignedIntArray(new long[] { maxUnsignedInt + 1 })).isInstanceOf(IllegalArgumentException.class);

        assertThat(Array.checkToUnsignedIntArray(new long[0])).isEmpty();
        assertThat(Array.checkToUnsignedIntArray(new long[] { 0L })).containsExactly(0);

        assertThat(Array.checkToUnsignedIntArray(new long[] { maxUnsignedInt })).containsExactly((int)(maxUnsignedInt));
        assertThat(Array.checkToUnsignedIntArray(new long[] { 0L })).containsExactly(0);
        assertThat(Array.checkToUnsignedIntArray(new long[] { 1L, 0L })).containsExactly(1, 0);
        assertThat(Array.checkToUnsignedIntArray(new long[] { 1L, 0L, 2L })).containsExactly(1, 0, 2);
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckToLongArray() {

        final int minUnsignedInt = Integer.MIN_VALUE;
        final int maxUnsignedInt = Integer.MAX_VALUE;

        assertThatThrownBy(() -> Array.toLongArray(null)).isInstanceOf(NullPointerException.class);

        assertThat(Array.toLongArray(new int[0])).isEmpty();
        assertThat(Array.toLongArray(new int[] { 0 })).containsExactly(0);

        assertThat(Array.toLongArray(new int[] { minUnsignedInt })).containsExactly(minUnsignedInt);
        assertThat(Array.toLongArray(new int[] { maxUnsignedInt })).containsExactly(maxUnsignedInt);
        assertThat(Array.toLongArray(new int[] { 0 })).containsExactly(0);
        assertThat(Array.toLongArray(new int[] { 1, 0 })).containsExactly(1, 0);
        assertThat(Array.toLongArray(new int[] { 1, 0, 2 })).containsExactly(1, 0, 2);
    }

    @Test
    @Category(UnitTest.class)
    public void testMove() {

        assertThatThrownBy(() -> Array.move(null, 0, 0, 0)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.move(new long[] { 0L }, 1, 1, -1)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(() -> Array.move(new long[] { 0L }, 0, 2, 1)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(() -> Array.move(new long[] { 0L }, 0, 1, -1)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(() -> Array.move(new long[] { 0L, 1 }, 1, 1, -2)).isInstanceOf(ArrayIndexOutOfBoundsException.class);

        assertThatThrownBy(() -> Array.move(new long[] { 0L }, 0, 0, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Array.move(new long[] { 0L }, 0, 1, 0)).isInstanceOf(IllegalArgumentException.class);

        checkLong(new long[] { 123L, 234L, 345L }, 0, 1, 1, new long[] { 123L, 123L, 345L });
        checkLong(new long[] { 123L, 234L, 345L }, 0, 2, 1, new long[] { 123L, 123L, 234L });
        checkLong(new long[] { 123L, 234L, 345L }, 0, 1, 2, new long[] { 123L, 234L, 123L });

        checkLong(new long[] { 123L, 234L, 345L }, 2, 1, -1, new long[] { 123L, 345L, 345L });
        checkLong(new long[] { 123L, 234L, 345L }, 1, 2, -1, new long[] { 234L, 345L, 345L });
        checkLong(new long[] { 123L, 234L, 345L }, 2, 1, -2, new long[] { 345L, 234L, 345L });
    }

    private void checkLong(long[] array, int startIndex, int numElements, int delta, long[] expectedResult) {

        Array.move(array, startIndex, numElements, delta);

        assertThat(array).isEqualTo(expectedResult);
    }

    @Override
    protected <T, R> R[] map(T[] array, IntFunction<R[]> createMappedArray, Function<T, R> mapper) {

        return Array.map(array, createMappedArray, mapper);
    }

    @Override
    protected <T> boolean equals(T[] array1, int startIndex1, T[] array2, int startIndex2, int numElements) {

        return Array.equals(array1, startIndex1, array2, startIndex2, numElements);
    }

    @Override
    protected <T, P> boolean equals(T[] array1, P parameter1, T[] array2, P parameter2, IByIndexTestEqualityTester<T, P> byIndexEqualityTester) {

        return Array.equals(array1, parameter1, array2, parameter2,
                byIndexEqualityTester != null
                        ? (e1, p1, e2, p2) -> byIndexEqualityTester.equals(e1, p1, e2, p2)
                        : null);
    }

    @Override
    protected <T, P> boolean equals(T[] array1, int startIndex1, P parameter1, T[] array2, int startIndex2, P parameter2, int numElements,
            IByIndexTestEqualityTester<T, P> byIndexEqualityTester) {

        return Array.equals(array1, startIndex1, parameter1, array2, startIndex2, parameter2, numElements,
                byIndexEqualityTester != null
                        ? (e1, p1, e2, p2) -> byIndexEqualityTester.equals(e1, p1, e2, p2)
                        : null);
    }

    @Override
    protected <T> boolean containsInstance(T[] array, T instance) {

        return Array.containsInstance(array, instance);
    }

    @Override
    protected <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance) {

        return Array.containsInstance(array, instance, startIndex, numElements);
    }

    @Override
    protected <T, P> int findAtMostOneIndex(T[] array, P parameter, BiPredicate<T, P> predicate) {

        return Array.findAtMostOneIndex(array, parameter, predicate);
    }

    @Override
    protected <T> int closureOrConstantFindAtMostOneIndex(T[] array, Predicate<T> predicate) {

        return Array.closureOrConstantFindAtMostOneIndex(array, predicate);
    }

    @Override
    protected <T, P> int findAtMostOneIndexInRange(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return Array.findAtMostOneIndex(array, parameter, startIndex, numElements, predicate);
    }

    @Override
    protected <T> int closureOrConstantFindAtMostOneIndexInRange(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return Array.closureOrConstantFindAtMostOneIndex(array, startIndex, numElements, predicate);
    }

    private static String[] create(String ... values) {

        return values;
    }

    private static int[] create(int ... values) {

        return values;
    }
}
