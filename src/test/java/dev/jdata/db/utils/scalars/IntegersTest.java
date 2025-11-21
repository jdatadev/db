package dev.jdata.db.utils.scalars;

import java.util.function.ToIntFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class IntegersTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testIntToHexUnsigned() {

        assertThatThrownBy(() -> Integers.toHexUnsigned(0, null)).isInstanceOf(NullPointerException.class);

        checkIntToHexUnsigned(Integer.MIN_VALUE, "80000000");
        checkIntToHexUnsigned(-1, "FFFFFFFF");
        checkIntToHexUnsigned(0, "0");
        checkIntToHexUnsigned(9, "9");
        checkIntToHexUnsigned(10, "A");
        checkIntToHexUnsigned(16, "10");
        checkIntToHexUnsigned(Integer.MAX_VALUE, "7FFFFFFF");

        for (int i = -1000 * 1000; i < 1000 * 1000; ++ i) {

            checkIntToHexUnsigned(i, Long.toHexString(Integer.toUnsignedLong(i)).toUpperCase());
        }
    }

    private static void checkIntToHexUnsigned(int i, String expectedString) {

        final StringBuilder sb = new StringBuilder(expectedString.length());

        Integers.toHexUnsigned(i, sb);

        assertThatCharSeq(sb).isEqualToCharSequence(expectedString);
    }

    @Test
    @Category(UnitTest.class)
    public void testLongToHexUnsigned() {

        assertThatThrownBy(() -> Integers.toHexUnsigned(0L, null)).isInstanceOf(NullPointerException.class);

        checkLongToHexUnsigned(Long.MIN_VALUE + 1, "8000000000000001");
        checkLongToHexUnsigned(Long.MIN_VALUE, "8000000000000000");
        checkLongToHexUnsigned(-1, "FFFFFFFFFFFFFFFF");
        checkLongToHexUnsigned(0, "0");
        checkLongToHexUnsigned(9, "9");
        checkLongToHexUnsigned(10, "A");
        checkLongToHexUnsigned(16, "10");
        checkLongToHexUnsigned(Long.MAX_VALUE, "7FFFFFFFFFFFFFFF");

        for (long l = -1000L * 1000L; l < 1000L * 1000L; ++ l) {

            checkLongToHexUnsigned(l, Long.toHexString(l).toUpperCase());
        }
    }

    private static void checkLongToHexUnsigned(long l, String expectedString) {

        final StringBuilder sb = new StringBuilder(expectedString.length());

        Integers.toHexUnsigned(l, sb);

        assertThatCharSeq(sb).isEqualToCharSequence(expectedString);
    }

    @Test
    @Category(UnitTest.class)
    public void testParseUnsignedInt() {

        checkParseUnsignedInt(Integers::parseUnsignedInt);
        checkParseUnsignedInt(s -> Integers.parseUnsignedInt((CharSequence)s));
        checkParseUnsignedInt(s -> Integers.parseUnsignedInt(s, 0, s.length(), 10));
    }

    private static void checkParseUnsignedInt(ToIntFunction<String> parse) {

        assertThatThrownBy(() -> parse.applyAsInt("-1")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt(String.valueOf(Integer.MAX_VALUE + 1L))).isInstanceOf(NumberFormatException.class);

        checkParseIntCommon(parse);
    }

    @Test
    @Category(UnitTest.class)
    public void testParseSignedInt() {

        checkParseSignedInt(Integers::parseSignedInt);
        checkParseSignedInt(s -> Integers.parseSignedInt((CharSequence)s));
        checkParseSignedInt(s -> Integers.parseSignedInt(s, 0, s.length(), 10));
    }

    private static void checkParseSignedInt(ToIntFunction<String> parse) {

        assertThatThrownBy(() -> parse.applyAsInt("-")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt("-0")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt("-00")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt(String.valueOf(Integer.MIN_VALUE))).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt(String.valueOf(Integer.MIN_VALUE - 1L))).isInstanceOf(NumberFormatException.class);

        checkParseIntCommon(parse);

        checkParseSignedInt("-1", -1, parse);
        checkParseSignedInt("-123", -123, parse);

        final int intMinValuePlusOne = Integer.MIN_VALUE + 1;

        checkParseSignedInt(String.valueOf(intMinValuePlusOne), intMinValuePlusOne, parse);
    }

    private static void checkParseSignedInt(String string, int expectedResult, ToIntFunction<String> parse) {

        assertThat(parse.applyAsInt(string)).isEqualTo(expectedResult);
    }

    private static void checkParseIntCommon(ToIntFunction<String> parse) {

        assertThatThrownBy(() -> parse.applyAsInt(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> parse.applyAsInt("")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt(" ")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt(" 1")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt("00")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt("01")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt("a")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> parse.applyAsInt(String.valueOf(Integer.MAX_VALUE + 1L))).isInstanceOf(NumberFormatException.class);

        checkParseInt("0", 0, parse);
        checkParseInt("1", 1, parse);
        checkParseInt("100", 100, parse);
        checkParseInt("123", 123, parse);
        checkParseInt(String.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE, parse);
    }

    private static void checkParseInt(String string, int expectedResult, ToIntFunction<String> parse) {

        assertThat(parse.applyAsInt(string)).isEqualTo(expectedResult);
    }

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
    public void testCheckUnsignedIntToUnsignedByteAsByte() {

        assertThat(Integers.checkUnsignedIntToUnsignedByteAsByte(0)).isEqualTo((byte)0);

        for (int i = 0; i < 8; ++ i) {

            assertThat(Integers.checkUnsignedIntToUnsignedByteAsByte(1 << i)).isEqualTo((byte)(1 << i));
        }

        for (int i = 8; i < 32; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkUnsignedIntToUnsignedByteAsByte(1 << closureI)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckUnsignedIntToUnsignedByteAsShort() {

        assertThat(Integers.checkUnsignedIntToUnsignedByteAsShort(0)).isEqualTo((short)0);

        for (int i = 0; i < 8; ++ i) {

            assertThat(Integers.checkUnsignedIntToUnsignedByteAsShort(1 << i)).isEqualTo((short)(1 << i));
        }

        for (int i = 8; i < 32; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkUnsignedIntToUnsignedByteAsShort(1 << closureI)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckIntToShort() {

        assertThatThrownBy(() -> Integers.checkIntToShort(Short.MIN_VALUE - 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Integers.checkIntToShort(Short.MAX_VALUE + 1)).isInstanceOf(IllegalArgumentException.class);

        assertThat(Integers.checkIntToShort(Short.MIN_VALUE)).isEqualTo(Short.MIN_VALUE);
        assertThat(Integers.checkIntToShort(0)).isEqualTo((short)0);
        assertThat(Integers.checkIntToShort(Short.MAX_VALUE)).isEqualTo(Short.MAX_VALUE);

        for (int i = 0; i < 15; ++ i) {

            assertThat(Integers.checkIntToShort(1 << i)).isEqualTo((short)(1 << i));
            assertThat(Integers.checkIntToShort(- (1 << i))).isEqualTo((short)(- (1 << i)));
        }

        assertThatThrownBy(() -> Integers.checkIntToShort(1 << 15)).isInstanceOf(IllegalArgumentException.class);

        for (int i = 16; i < 31; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkIntToShort(1 << closureI)).isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> Integers.checkIntToShort(- (1 << closureI))).isInstanceOf(IllegalArgumentException.class);
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
    public void testCheckUnsignedLongToUnsignedByteAsByte() {

        assertThat(Integers.checkUnsignedLongToUnsignedByteAsByte(0L)).isEqualTo((byte)0);

        for (int i = 0; i < 8; ++ i) {

            assertThat(Integers.checkUnsignedLongToUnsignedByteAsByte(1L << i)).isEqualTo((byte)(1 << i));
        }

        for (int i = 8; i < 64; ++ i) {

            final int closureI = i;

            assertThatThrownBy(() -> Integers.checkUnsignedLongToUnsignedByteAsByte(1L << closureI)).isInstanceOf(IllegalArgumentException.class);
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
