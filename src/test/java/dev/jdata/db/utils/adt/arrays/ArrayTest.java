package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class ArrayTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testIsArrayCapacity() {

        assertThatThrownBy(() -> Array.isArrayCapacity(-1L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Array.isArrayCapacity(0L)).isTrue();
        assertThat(Array.isArrayCapacity(1L)).isTrue();
        assertThat(Array.isArrayCapacity(Integer.MAX_VALUE)).isTrue();
        assertThat(Array.isArrayCapacity(Integer.MAX_VALUE + 1L)).isFalse();
    }
}
