package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class ArrayTest extends BaseTest {

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
}
