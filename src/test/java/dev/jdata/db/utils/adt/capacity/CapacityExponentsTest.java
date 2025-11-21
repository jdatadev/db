package dev.jdata.db.utils.adt.capacity;

import java.util.function.LongToIntFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.function.IntToIntFunction;

public final class CapacityExponentsTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testComputeIntCapacityFromExponent() {

        final int maxCapacityExponent = 30;

        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityFromExponent(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityFromExponent(maxCapacityExponent + 1)).isInstanceOf(IllegalArgumentException.class);

        for (int i = 0; i <= maxCapacityExponent; ++ i) {

            assertThat(CapacityExponents.computeIntCapacityFromExponent(i)).isEqualTo(powInt(2, i));
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeIntCapacityExponentForAtOrAboveZero() {

        assertThat(CapacityExponents.computeIntCapacityExponentForAtOrAboveZero(0)).isEqualTo(0);

        checkComputeIntCapacityExponent(false, CapacityExponents::computeIntCapacityExponentForAtOrAboveZero);
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeIntCapacityExponentForAboveZero() {

        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentForAboveZero(0)).isInstanceOf(IllegalArgumentException.class);

        checkComputeIntCapacityExponent(true, CapacityExponents::computeIntCapacityExponentForAboveZero);
    }

    private static void checkComputeIntCapacityExponent(boolean aboveZero, IntToIntFunction compute) {

        assertThatThrownBy(() -> compute.apply(-1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(compute.apply(1)).isEqualTo(0);
        assertThat(compute.apply(2)).isEqualTo(1);
        assertThat(compute.apply(3)).isEqualTo(2);
        assertThat(compute.apply(4)).isEqualTo(2);
        assertThat(compute.apply(5)).isEqualTo(3);
        assertThat(compute.apply(6)).isEqualTo(3);
        assertThat(compute.apply(7)).isEqualTo(3);
        assertThat(compute.apply(8)).isEqualTo(3);

        for (int i = aboveZero ? 1 : 0; i < 31; ++ i) {

            final int numElements = powInt(2, i);

            if (numElements - 1 != powLong(2, i - 1)) {

                assertThat(compute.apply(numElements - 1)).isEqualTo(i);
            }

            assertThat(compute.apply(numElements)).isEqualTo(i);
            assertThat(compute.apply(numElements + 1)).isEqualTo(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeIntCapacityExponentExactForAboveZero() {

        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(0)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeIntCapacityExponentExactForAboveZero(1)).isEqualTo(0);
        assertThat(CapacityExponents.computeIntCapacityExponentExactForAboveZero(2)).isEqualTo(1);

        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(3)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeIntCapacityExponentExactForAboveZero(4)).isEqualTo(2);

        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(5)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(6)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(7)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeIntCapacityExponentExactForAboveZero(8)).isEqualTo(3);

        for (int i = 0; i < 31; ++ i) {

            final int numElements = powInt(2, i);

            if (numElements - 1 != powInt(2, i - 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(numElements - 1)).isInstanceOf(IllegalArgumentException.class);
            }

            assertThat(CapacityExponents.computeIntCapacityExponentExactForAboveZero(numElements)).isEqualTo(i);

            if (numElements + 1 != powInt(2, i + 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeIntCapacityExponentExactForAboveZero(numElements + 1)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    private static int powInt(int integer, int exponent) {

        int result = 1;

        for (int i = 0; i < exponent; ++ i) {

            result *= integer;
        }

        return result;
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeLongCapacityExponentForAtOrAboveZero() {

        assertThat(CapacityExponents.computeLongCapacityExponentForAtOrAboveZero(0L)).isEqualTo(0);

        checkComputeLongCapacityExponent(false, CapacityExponents::computeLongCapacityExponentForAtOrAboveZero);
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeLongCapacityExponentForAboveZero() {

        assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentForAboveZero(0L)).isInstanceOf(IllegalArgumentException.class);

        checkComputeLongCapacityExponent(true, CapacityExponents::computeLongCapacityExponentForAboveZero);
    }

    private static void checkComputeLongCapacityExponent(boolean aboveZero, LongToIntFunction compute) {

        assertThatThrownBy(() -> compute.applyAsInt(-1L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(compute.applyAsInt(1L)).isEqualTo(0);
        assertThat(compute.applyAsInt(2L)).isEqualTo(1);
        assertThat(compute.applyAsInt(3L)).isEqualTo(2);
        assertThat(compute.applyAsInt(4L)).isEqualTo(2);
        assertThat(compute.applyAsInt(5L)).isEqualTo(3);
        assertThat(compute.applyAsInt(6L)).isEqualTo(3);
        assertThat(compute.applyAsInt(7L)).isEqualTo(3);
        assertThat(compute.applyAsInt(8L)).isEqualTo(3);

        for (int i = aboveZero ? 1 : 0; i < 63; ++ i) {

            final long numElements = powLong(2, i);

            if (numElements - 1 != powLong(2, i - 1)) {

                assertThat(compute.applyAsInt(numElements - 1)).isEqualTo(i);
            }

            assertThat(compute.applyAsInt(numElements)).isEqualTo(i);
            assertThat(compute.applyAsInt(numElements + 1)).isEqualTo(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeLongCapacityExponentExactForAboveZero() {

        assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(-1L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(0L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeLongCapacityExponentExactForAboveZero(1L)).isEqualTo(0);
        assertThat(CapacityExponents.computeLongCapacityExponentExactForAboveZero(2L)).isEqualTo(1);

        assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(3L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeLongCapacityExponentExactForAboveZero(4L)).isEqualTo(2);

        assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(5L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(6L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(7L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeLongCapacityExponentExactForAboveZero(8L)).isEqualTo(3);

        for (int i = 0; i < 63; ++ i) {

            final long numElements = powLong(2, i);

            if (numElements - 1 != powLong(2, i - 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(numElements - 1)).isInstanceOf(IllegalArgumentException.class);
            }

            assertThat(CapacityExponents.computeLongCapacityExponentExactForAboveZero(numElements)).isEqualTo(i);

            if (numElements + 1 != powLong(2, i + 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeLongCapacityExponentExactForAboveZero(numElements + 1)).isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    private static long powLong(long integer, int exponent) {

        long result = 1L;

        for (int i = 0; i < exponent; ++ i) {

            result *= integer;
        }

        return result;
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeArrayOuterCapacity() {

        assertThatThrownBy(() -> CapacityExponents.computeArrayOuterCapacity(-1, 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeArrayOuterCapacity(1, -1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeArrayOuterCapacity(0, 0)).isEqualTo(0);
        assertThat(CapacityExponents.computeArrayOuterCapacity(1, 0)).isEqualTo(1);
        assertThat(CapacityExponents.computeArrayOuterCapacity(2, 0)).isEqualTo(2);
        assertThat(CapacityExponents.computeArrayOuterCapacity(3, 0)).isEqualTo(3);

        assertThat(CapacityExponents.computeArrayOuterCapacity(0, 1)).isEqualTo(0);
        assertThat(CapacityExponents.computeArrayOuterCapacity(1, 1)).isEqualTo(1);
        assertThat(CapacityExponents.computeArrayOuterCapacity(2, 1)).isEqualTo(1);
        assertThat(CapacityExponents.computeArrayOuterCapacity(3, 1)).isEqualTo(2);
        assertThat(CapacityExponents.computeArrayOuterCapacity(4, 1)).isEqualTo(2);
        assertThat(CapacityExponents.computeArrayOuterCapacity(5, 1)).isEqualTo(3);

        assertThat(CapacityExponents.computeArrayOuterCapacity(0, 2)).isEqualTo(0);
        assertThat(CapacityExponents.computeArrayOuterCapacity(1, 2)).isEqualTo(1);
        assertThat(CapacityExponents.computeArrayOuterCapacity(2, 2)).isEqualTo(1);
        assertThat(CapacityExponents.computeArrayOuterCapacity(3, 2)).isEqualTo(1);
        assertThat(CapacityExponents.computeArrayOuterCapacity(4, 2)).isEqualTo(1);
        assertThat(CapacityExponents.computeArrayOuterCapacity(5, 2)).isEqualTo(2);
        assertThat(CapacityExponents.computeArrayOuterCapacity(6, 2)).isEqualTo(2);
        assertThat(CapacityExponents.computeArrayOuterCapacity(7, 2)).isEqualTo(2);
        assertThat(CapacityExponents.computeArrayOuterCapacity(8, 2)).isEqualTo(2);
        assertThat(CapacityExponents.computeArrayOuterCapacity(9, 2)).isEqualTo(3);
    }

    @Test
    @Category(UnitTest.class)
    public void testIntKeyMask() {

        final int maxCapacityExponent = 30;

        assertThatThrownBy(() -> CapacityExponents.makeIntKeyMask(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.makeIntKeyMask(maxCapacityExponent + 1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.makeIntKeyMask(0)).isEqualTo(0b0);
        assertThat(CapacityExponents.makeIntKeyMask(1)).isEqualTo(0b1);
        assertThat(CapacityExponents.makeIntKeyMask(2)).isEqualTo(0b11);
        assertThat(CapacityExponents.makeIntKeyMask(3)).isEqualTo(0b111);
        assertThat(CapacityExponents.makeIntKeyMask(4)).isEqualTo(0b1111);
        assertThat(CapacityExponents.makeIntKeyMask(5)).isEqualTo(0b11111);
        assertThat(CapacityExponents.makeIntKeyMask(6)).isEqualTo(0b111111);
        assertThat(CapacityExponents.makeIntKeyMask(7)).isEqualTo(0b1111111);
        assertThat(CapacityExponents.makeIntKeyMask(8)).isEqualTo(0b11111111);

        for (int i = 0; i <= maxCapacityExponent; ++ i) {

            assertThat(CapacityExponents.makeIntKeyMask(i)).isEqualTo(BitsUtil.maskInt(i, 0));
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testLongKeyMask() {

        final int maxCapacityExponent = 30;

        assertThatThrownBy(() -> CapacityExponents.makeLongKeyMask(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.makeLongKeyMask(maxCapacityExponent + 1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.makeLongKeyMask(0)).isEqualTo(0b0);
        assertThat(CapacityExponents.makeLongKeyMask(1)).isEqualTo(0b1);
        assertThat(CapacityExponents.makeLongKeyMask(2)).isEqualTo(0b11);
        assertThat(CapacityExponents.makeLongKeyMask(3)).isEqualTo(0b111);
        assertThat(CapacityExponents.makeLongKeyMask(4)).isEqualTo(0b1111);
        assertThat(CapacityExponents.makeLongKeyMask(5)).isEqualTo(0b11111);
        assertThat(CapacityExponents.makeLongKeyMask(6)).isEqualTo(0b111111);
        assertThat(CapacityExponents.makeLongKeyMask(7)).isEqualTo(0b1111111);
        assertThat(CapacityExponents.makeLongKeyMask(8)).isEqualTo(0b11111111);

        for (int i = 0; i <= maxCapacityExponent; ++ i) {

            assertThat(CapacityExponents.makeLongKeyMask(i)).isEqualTo(BitsUtil.maskLong(i, 0));
        }
    }
}
