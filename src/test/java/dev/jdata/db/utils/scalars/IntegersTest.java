package dev.jdata.db.utils.scalars;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class IntegersTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testCheckIntToByte() {

        assertThatThrownBy(() -> Integers.checkIntToByte(Byte.MIN_VALUE - 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Integers.checkIntToByte(Byte.MAX_VALUE + 1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Integers.checkIntToByte(Byte.MIN_VALUE)).isEqualTo(Byte.MIN_VALUE);
        assertThat(Integers.checkIntToByte(0)).isEqualTo((byte)0);
        assertThat(Integers.checkIntToByte(Byte.MAX_VALUE)).isEqualTo(Byte.MAX_VALUE);

        for (int i = 0; i < 7; ++ i) {

            assertThat(Integers.checkIntToByte(1 << i)).isEqualTo((byte)(1 << i));
            assertThat(Integers.checkIntToByte(- (1 << i))).isEqualTo((byte)(- (1 << i)));
        }

        assertThatThrownBy(() -> Integers.checkIntToByte(1 << 7)).isInstanceOf(IllegalArgumentException.class);

        for (int i = 8; i < 31; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkIntToByte(1 << closureI)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> Integers.checkIntToByte(- (1 << closureI))).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckUnsignedIntToUnsignedByte() {

        assertThat(Integers.checkUnsignedIntToUnsignedByte(0)).isEqualTo((byte)0);

        for (int i = 0; i < 8; ++ i) {

            assertThat(Integers.checkUnsignedIntToUnsignedByte(1 << i)).isEqualTo((byte)(1 << i));
        }

        for (int i = 8; i < 32; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkUnsignedIntToUnsignedByte(1 << closureI)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckUnsignedIntToUnsignedShort() {

        assertThat(Integers.checkUnsignedIntToUnsignedShort(0)).isEqualTo((short)0);

        for (int i = 0; i < 15; ++ i) {

            assertThat(Integers.checkUnsignedIntToUnsignedShort(1 << i)).isEqualTo((short)(1 << i));
        }

        for (int i = 15; i < 32; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkUnsignedIntToUnsignedShort(1 << closureI)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckLongToInt() {

        assertThatThrownBy(() -> Integers.checkLongToInt(Integer.MIN_VALUE - 1L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Integers.checkLongToInt(Integer.MAX_VALUE + 1L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Integers.checkLongToInt(Integer.MIN_VALUE)).isEqualTo(Integer.MIN_VALUE);
        assertThat(Integers.checkLongToInt(0L)).isEqualTo(0);
        assertThat(Integers.checkLongToInt(Integer.MAX_VALUE)).isEqualTo(Integer.MAX_VALUE);

        for (int i = 0; i < 31; ++ i) {

            assertThat(Integers.checkLongToInt(1L << i)).isEqualTo(1 << i);
            assertThat(Integers.checkLongToInt(- (1L << i))).isEqualTo(- (1 << i));
        }

        assertThatThrownBy(() -> Integers.checkLongToInt(1L << 31)).isInstanceOf(IllegalArgumentException.class);

        for (int i = 32; i < 63; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkLongToInt(1L << closureI)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> Integers.checkLongToInt(- (1L << closureI))).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckUnsignedLongToUnsignedInt() {

        assertThat(Integers.checkUnsignedLongToUnsignedInt(0L)).isEqualTo(0);

        for (int i = 0; i < 31; ++ i) {

            assertThat(Integers.checkUnsignedLongToUnsignedInt(1L << i)).isEqualTo(1 << i);
        }

        for (int i = 31; i < 64; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkUnsignedLongToUnsignedInt(1L << closureI)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testToChars() {

        assertThatThrownBy(() -> Integers.toChars(1L, new StringBuilder(), null)).isInstanceOf(NullPointerException.class);

        checkToChars(Long.MIN_VALUE, Long.toString(Long.MIN_VALUE));
        checkToChars(Long.MIN_VALUE + 1, Long.toString(Long.MIN_VALUE + 1));
        checkToChars(-1L, "-1");
        checkToChars(0L, "0");
        checkToChars(1L, "1");
        checkToChars(12L, "12");
        checkToChars(123L, "123");
        checkToChars(10L, "10");
        checkToChars(100L, "100");
        checkToChars(101L, "101");
        checkToChars(1010L, "1010");
        checkToChars(Long.MAX_VALUE, Long.toString(Long.MAX_VALUE));
    }

    private void checkToChars(long integer, String expectedValue) {

        final StringBuilder sb = new StringBuilder(30);

        Integers.toChars(integer, sb, (c, p) -> p.append(c));

        assertThat(sb.toString()).isEqualTo(expectedValue);
    }
}
