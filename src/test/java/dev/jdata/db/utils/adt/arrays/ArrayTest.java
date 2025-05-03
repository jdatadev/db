package dev.jdata.db.utils.adt.arrays;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.BaseByIndexTest;

public final class ArrayTest extends BaseByIndexTest {

    public ArrayTest() {
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
    public void testMax() {

        assertThatThrownBy(() -> Array.max(null, 0, Integer::intValue)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.max(new Integer[] { 1, 2, 3 }, 0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> Array.max(new Integer[] { 1, 2, 3 }, 0, null)).isInstanceOf(NullPointerException.class);

        assertThat(Array.max(new Integer[0], 0, Integer::intValue)).isEqualTo(0);
        assertThat(Array.max(new Integer[0], 123, Integer::intValue)).isEqualTo(123);

        assertThat(Array.max(new Integer[] { 0 },               123, Integer::intValue)).isEqualTo(0);
        assertThat(Array.max(new Integer[] { 0, 1 },            123, Integer::intValue)).isEqualTo(1);
        assertThat(Array.max(new Integer[] { 1, 2 ,0 },         123, Integer::intValue)).isEqualTo(2);
        assertThat(Array.max(new Integer[] { 1, 0, -2 },        123, Integer::intValue)).isEqualTo(1);
        assertThat(Array.max(new Integer[] { 1, 0, 1, -2 },     123, Integer::intValue)).isEqualTo(1);
        assertThat(Array.max(new Integer[] { -1, 0, -1, -2 },   123, Integer::intValue)).isEqualTo(0);
        assertThat(Array.max(new Integer[] { -1, -3, -1, -2 },  123, Integer::intValue)).isEqualTo(-1);
    }

    @Test
    @Category(UnitTest.class)
    public void testBoxedIntArray() {

        checkCopyElements(Function.identity(), Array::boxed, (a, i) -> a[i], a -> a.length);
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

    @Override
    protected <T, R> R[] map(T[] array, IntFunction<R[]> createMappedArray, Function<T, R> mapper) {

        return Array.map(array, createMappedArray, mapper);
    }

    @Override
    protected <T> boolean containsInstance(T[] array, T instance) {

        return Array.containsInstance(array, instance);
    }

    @Override
    protected <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance) {

        return Array.containsInstance(array, startIndex, numElements, instance);
    }

    @Override
    protected <T, P> int findIndex(T[] array, P parameter, BiPredicate<T, P> predicate) {

        return Array.findIndex(array, parameter, predicate);
    }

    @Override
    protected <T> int closureOrConstantFindIndex(T[] array, Predicate<T> predicate) {

        return Array.closureOrConstantFindIndex(array, predicate);
    }

    @Override
    protected <T, P> int findIndexInRange(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return Array.findIndex(array, parameter, startIndex, numElements, predicate);
    }

    @Override
    protected <T> int closureOrConstantFindIndexInRange(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return Array.closureOrConstantFindIndex(array, startIndex, numElements, predicate);
    }

    private static String[] create(String ... values) {

        return values;
    }

    private static int[] create(int ... values) {

        return values;
    }
}
