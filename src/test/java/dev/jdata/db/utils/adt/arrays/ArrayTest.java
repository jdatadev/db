package dev.jdata.db.utils.adt.arrays;

import java.util.function.Function;
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
    public final void testBoxedIntArray() {

        checkCopyElements(Function.identity(), Array::boxed, (a, i) -> a[i], a -> a.length);
    }

    @Test
    @Category(UnitTest.class)
    public final void testCopyIntArray() {

        checkCopyElements(Function.identity(), Array::copyOf, (a, i) -> a[i], a -> a.length);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyLongArray() {

        checkCopyElements(a -> Array.toLongArray(a), Array::copyOf, (a, i) -> a[i], a -> a.length, (e, i) -> e == i);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyElements() {

        checkCopyElements(Array::boxed, Array::copyOf, (a, i) -> a[i], a -> a.length, (e, i) -> e == i);
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

    @Override
    protected <T> boolean containsInstance(T[] array, T instance) {

        return Array.containsInstance(array, instance);
    }

    @Override
    protected <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance) {

        return Array.containsInstance(array, startIndex, numElements, instance);
    }

    @Override
    protected <T> int findIndex(T[] array, Predicate<T> predicate) {

        return Array.findIndex(array, predicate);
    }

    @Override
    protected <T> int findIndexRange(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return Array.findIndex(array, startIndex, numElements, predicate);
    }
}
