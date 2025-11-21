package dev.jdata.db.utils.jdk.adt.strings;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.jdk.adt.strings.StringBuilders.Case;

public final class StringBuildersTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testIsEmpty() {

        assertThatThrownBy(() -> StringBuilders.isEmpty(null)).isInstanceOf(NullPointerException.class);

        checkIsEmpty("", true);
        checkIsEmpty(" ", false);
        checkIsEmpty("\t", false);
        checkIsEmpty("\0", false);
        checkIsEmpty("a", false);
        checkIsEmpty("abc", false);
    }

    private static void checkIsEmpty(String string, boolean expectedResult) {

        final StringBuilder sb = new StringBuilder(string);

        assertThat(StringBuilders.isEmpty(sb)).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public void testRepeat() {

        assertThatThrownBy(() -> checkRepeatArguments(null, " ", 1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> checkRepeatArguments(new StringBuilder(), null, 1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> checkRepeatArguments(new StringBuilder(), "", 1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> checkRepeatArguments(new StringBuilder(), " ", -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> checkRepeatArguments(new StringBuilder(), " ", 0)).isInstanceOf(IllegalArgumentException.class);

        checkRepeat("", " ", 1, " ");
        checkRepeat("", " ", 2, "  ");
        checkRepeat("", " ", 3, "   ");

        checkRepeat("", "  ", 1, "  ");
        checkRepeat("", "  ", 2, "    ");
        checkRepeat("", "  ", 3, "      ");

        checkRepeat("abc", "123", 1, "abc123");
        checkRepeat("abc", "123", 2, "abc123123");
        checkRepeat("abc", "123", 3, "abc123123123");
    }

    private static void checkRepeatArguments(StringBuilder sb, String toAdd, int times) {

        final String sbContents = sb != null ? sb.toString() : null;

        try {
            StringBuilders.repeat(sb, toAdd, times);
        }
        finally {

            if (sbContents != null) {

                assertThat(sb.toString()).isEqualTo(sbContents);
            }
        }
    }

    private static void checkRepeat(String prefix, String toAdd, int times, String expectedResult) {

        final StringBuilder sb = new StringBuilder(prefix);

        StringBuilders.repeat(sb, toAdd, times);

        assertThat(sb.toString()).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public void testHexString() {

        assertThatThrownBy(() -> StringBuilders.hexString(null, 0L, false)).isInstanceOf(NullPointerException.class);

        checkHexString(0L, false, "0");
        checkHexString(0L, true, "0x0");
        checkHexString(1L, false, "1");
        checkHexString(1L, true, "0x1");
        checkHexString(10L, false, "A");
        checkHexString(10L, true, "0xA");

        assertThatThrownBy(() -> StringBuilders.hexString(null, 0L, false, 0)).isInstanceOf(NullPointerException.class);

        checkHexString(0L, false, 0, "0");
        checkHexString(0L, true, 0, "0x0");
        checkHexString(0L, false, 1, "0");
        checkHexString(0L, true, 1, "0x0");
        checkHexString(0L, false, 2, "00");
        checkHexString(0L, true, 2, "0x00");
        checkHexString(1L, false, 0, "1");
        checkHexString(1L, true, 0, "0x1");
        checkHexString(1L, false, 1, "1");
        checkHexString(1L, true, 1, "0x1");
        checkHexString(1L, false, 2, "01");
        checkHexString(1L, true, 2, "0x01");
        checkHexString(10L, false, 0, "A");
        checkHexString(10L, true, 0, "0xA");
        checkHexString(10L, false, 1, "A");
        checkHexString(10L, true, 1, "0xA");
        checkHexString(10L, false, 2, "0A");
        checkHexString(10L, true, 2, "0x0A");

        assertThatThrownBy(() -> StringBuilders.hexString(null, 0L, "", 0, Case.UPPER)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> StringBuilders.hexString(new StringBuilder(), 0L, "", 0, null)).isInstanceOf(NullPointerException.class);

        checkHexString(0L, null, 0, Case.UPPER, "0");
        checkHexString(0L, "hex", 0, Case.UPPER, "hex0");

        checkHexString(1L, "hex", 4, Case.UPPER, "hex0001");
        checkHexString(9L, "hex", 4, Case.UPPER, "hex0009");
        checkHexString(10L, "hex", 4, Case.UPPER, "hex000A");
        checkHexString(11L, "hex", 4, Case.UPPER, "hex000B");
        checkHexString(12L, "hex", 4, Case.UPPER, "hex000C");
        checkHexString(13L, "hex", 4, Case.UPPER, "hex000D");
        checkHexString(14L, "hex", 4, Case.UPPER, "hex000E");
        checkHexString(15L, "hex", 4, Case.UPPER, "hex000F");
        checkHexString(16L, "hex", 4, Case.UPPER, "hex0010");
        checkHexString(255L, "hex", 4, Case.UPPER, "hex00FF");
        checkHexString(256L, "hex", 4, Case.UPPER, "hex0100");

        checkHexString(-1L, "hex", 4, Case.UPPER, "hex-0001");
        checkHexString(-9L, "hex", 4, Case.UPPER, "hex-0009");
        checkHexString(-10L, "hex", 4, Case.UPPER, "hex-000A");
        checkHexString(-15L, "hex", 4, Case.UPPER, "hex-000F");
        checkHexString(-16L, "hex", 4, Case.UPPER, "hex-0010");
        checkHexString(-255L, "hex", 4, Case.UPPER, "hex-00FF");
        checkHexString(-256L, "hex", 4, Case.UPPER, "hex-0100");

        checkHexString(10L, "hex", 4, Case.LOWER, "hex000a");
        checkHexString(11L, "hex", 4, Case.LOWER, "hex000b");
        checkHexString(12L, "hex", 4, Case.LOWER, "hex000c");
        checkHexString(13L, "hex", 4, Case.LOWER, "hex000d");
        checkHexString(14L, "hex", 4, Case.LOWER, "hex000e");
        checkHexString(15L, "hex", 4, Case.LOWER, "hex000f");

        for (int i = 0; i < 10; ++ i) {

            checkHexString(0L, "hex", i, Case.UPPER, "hex" + makeZeroPad(i - 1) + '0');
            checkHexString(1L, "hex", i, Case.UPPER, "hex" + makeZeroPad(i - 1) + '1');
            checkHexString(256L, "hex", i, Case.UPPER, "hex" + makeZeroPad(i - 3) + "100");
        }
    }

    private static String makeZeroPad(int numCharacters) {

        return numCharacters <= 0 ? "" : Strings.repeat('0', numCharacters);
    }

    private static void checkHexString(long value, boolean addPrefix, String expectedString) {

        final StringBuilder sb = new StringBuilder();

        StringBuilders.hexString(sb, value, addPrefix);

        assertThatCharSeq(sb).isEqualToCharSequence(expectedString);
    }

    private static void checkHexString(long value, boolean addPrefix, int zeroPad, String expectedString) {

        final StringBuilder sb = new StringBuilder();

        StringBuilders.hexString(sb, value, addPrefix, zeroPad);

        assertThatCharSeq(sb).isEqualToCharSequence(expectedString);
    }

    private static void checkHexString(long value, String prefix, int zeroPad, Case hexCase, String expectedString) {

        checkHexString(null, value, prefix, zeroPad, hexCase, expectedString);

        final String prepended = Strings.repeat("prepended", 10);

        checkHexString(prepended, value, prefix, zeroPad, hexCase, prepended + expectedString);
    }

    private static void checkHexString(String prepend, long value, String prefix, int zeroPad, Case hexCase, String expectedString) {

        final StringBuilder sb = new StringBuilder();

        if (prepend != null) {

            sb.append(prepend);
        }

        StringBuilders.hexString(sb, value, prefix, zeroPad, hexCase);

        assertThatCharSeq(sb).isEqualToCharSequence(expectedString);
    }
}
