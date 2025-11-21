package dev.jdata.db.utils.adt.numbers;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class ILargeNumberTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testCountDigits() {

        assertThat(ILargeNumberView.countDigits(-101L)).isEqualTo(3);
        assertThat(ILargeNumberView.countDigits(-100L)).isEqualTo(3);
        assertThat(ILargeNumberView.countDigits(-99L)).isEqualTo(2);
        assertThat(ILargeNumberView.countDigits(-11L)).isEqualTo(2);
        assertThat(ILargeNumberView.countDigits(-10L)).isEqualTo(2);
        assertThat(ILargeNumberView.countDigits(-9L)).isEqualTo(1);
        assertThat(ILargeNumberView.countDigits(-1L)).isEqualTo(1);

        assertThat(ILargeNumberView.countDigits(0L)).isEqualTo(1);

        assertThat(ILargeNumberView.countDigits(1L)).isEqualTo(1);
        assertThat(ILargeNumberView.countDigits(9L)).isEqualTo(1);
        assertThat(ILargeNumberView.countDigits(10L)).isEqualTo(2);
        assertThat(ILargeNumberView.countDigits(11L)).isEqualTo(2);
        assertThat(ILargeNumberView.countDigits(99L)).isEqualTo(2);
        assertThat(ILargeNumberView.countDigits(100L)).isEqualTo(3);
        assertThat(ILargeNumberView.countDigits(101L)).isEqualTo(3);
    }
}
