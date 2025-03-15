package dev.jdata.db.utils.adt.numbers;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class ILargeNumberTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testCountDigits() {

        assertThat(ILargeNumber.countDigits(-101L)).isEqualTo(3);
        assertThat(ILargeNumber.countDigits(-100L)).isEqualTo(3);
        assertThat(ILargeNumber.countDigits(-99L)).isEqualTo(2);
        assertThat(ILargeNumber.countDigits(-11L)).isEqualTo(2);
        assertThat(ILargeNumber.countDigits(-10L)).isEqualTo(2);
        assertThat(ILargeNumber.countDigits(-9L)).isEqualTo(1);
        assertThat(ILargeNumber.countDigits(-1L)).isEqualTo(1);

        assertThat(ILargeNumber.countDigits(0L)).isEqualTo(1);

        assertThat(ILargeNumber.countDigits(1L)).isEqualTo(1);
        assertThat(ILargeNumber.countDigits(9L)).isEqualTo(1);
        assertThat(ILargeNumber.countDigits(10L)).isEqualTo(2);
        assertThat(ILargeNumber.countDigits(11L)).isEqualTo(2);
        assertThat(ILargeNumber.countDigits(99L)).isEqualTo(2);
        assertThat(ILargeNumber.countDigits(100L)).isEqualTo(3);
        assertThat(ILargeNumber.countDigits(101L)).isEqualTo(3);
    }
}
