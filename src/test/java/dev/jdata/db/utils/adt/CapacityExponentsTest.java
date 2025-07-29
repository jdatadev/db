package dev.jdata.db.utils.adt;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.bits.BitsUtil;

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
    public void testComputeCapacityExponentInt() {

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponent(-1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponent(0)).isEqualTo(0);
        assertThat(CapacityExponents.computeCapacityExponent(1)).isEqualTo(0);
        assertThat(CapacityExponents.computeCapacityExponent(2)).isEqualTo(1);
        assertThat(CapacityExponents.computeCapacityExponent(3)).isEqualTo(2);
        assertThat(CapacityExponents.computeCapacityExponent(4)).isEqualTo(2);
        assertThat(CapacityExponents.computeCapacityExponent(5)).isEqualTo(3);
        assertThat(CapacityExponents.computeCapacityExponent(6)).isEqualTo(3);
        assertThat(CapacityExponents.computeCapacityExponent(7)).isEqualTo(3);
        assertThat(CapacityExponents.computeCapacityExponent(8)).isEqualTo(3);

        for (int i = 0; i < 31; ++ i) {

            final int numElements = powInt(2, i);

            if (numElements - 1 != powLong(2, i - 1)) {

                assertThat(CapacityExponents.computeCapacityExponent(numElements - 1)).isEqualTo(i);
            }

            assertThat(CapacityExponents.computeCapacityExponent(numElements)).isEqualTo(i);
            assertThat(CapacityExponents.computeCapacityExponent(numElements + 1)).isEqualTo(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeCapacityExponentExactInt() {

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(-1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(0)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponentExact(1)).isEqualTo(0);
        assertThat(CapacityExponents.computeCapacityExponentExact(2)).isEqualTo(1);

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(3)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponentExact(4)).isEqualTo(2);

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(5)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(6)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(7)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponentExact(8)).isEqualTo(3);

        for (int i = 0; i < 31; ++ i) {

            final int numElements = powInt(2, i);

            if (numElements - 1 != powInt(2, i - 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(numElements - 1)).isInstanceOf(IllegalArgumentException.class);
            }

            assertThat(CapacityExponents.computeCapacityExponentExact(numElements)).isEqualTo(i);

            if (numElements + 1 != powInt(2, i + 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(numElements + 1)).isInstanceOf(IllegalArgumentException.class);
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
    public void testComputeCapacityExponentLong() {

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponent(-1L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponent(0L)).isEqualTo(0);
        assertThat(CapacityExponents.computeCapacityExponent(1L)).isEqualTo(0);
        assertThat(CapacityExponents.computeCapacityExponent(2L)).isEqualTo(1);
        assertThat(CapacityExponents.computeCapacityExponent(3L)).isEqualTo(2);
        assertThat(CapacityExponents.computeCapacityExponent(4L)).isEqualTo(2);
        assertThat(CapacityExponents.computeCapacityExponent(5L)).isEqualTo(3);
        assertThat(CapacityExponents.computeCapacityExponent(6L)).isEqualTo(3);
        assertThat(CapacityExponents.computeCapacityExponent(7L)).isEqualTo(3);
        assertThat(CapacityExponents.computeCapacityExponent(8L)).isEqualTo(3);

        for (int i = 0; i < 63; ++ i) {

            final long numElements = powLong(2, i);

            if (numElements - 1 != powLong(2, i - 1)) {

                assertThat(CapacityExponents.computeCapacityExponent(numElements - 1)).isEqualTo(i);
            }

            assertThat(CapacityExponents.computeCapacityExponent(numElements)).isEqualTo(i);
            assertThat(CapacityExponents.computeCapacityExponent(numElements + 1)).isEqualTo(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testComputeCapacityExponentExactLong() {

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(-1L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(0L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponentExact(1L)).isEqualTo(0);
        assertThat(CapacityExponents.computeCapacityExponentExact(2L)).isEqualTo(1);

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(3L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponentExact(4L)).isEqualTo(2);

        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(5L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(6L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(7L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(CapacityExponents.computeCapacityExponentExact(8L)).isEqualTo(3);

        for (int i = 0; i < 63; ++ i) {

            final long numElements = powLong(2, i);

            if (numElements - 1 != powLong(2, i - 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(numElements - 1)).isInstanceOf(IllegalArgumentException.class);
            }

            assertThat(CapacityExponents.computeCapacityExponentExact(numElements)).isEqualTo(i);

            if (numElements + 1 != powLong(2, i + 1)) {

                assertThatThrownBy(() -> CapacityExponents.computeCapacityExponentExact(numElements + 1)).isInstanceOf(IllegalArgumentException.class);
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
