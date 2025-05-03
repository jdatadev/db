package dev.jdata.db.utils.adt.strings;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.strings.StringBuilders.Case;

public final class StringBuildersTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testHexString() {

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

        for (int i = 3; i < 10; ++ i) {

            checkHexString(0L, "hex", i, Case.UPPER, "hex" + "0".repeat(i - 1) + '0');
            checkHexString(1L, "hex", i, Case.UPPER, "hex" + "0".repeat(i - 1) + '1');
            checkHexString(256L, "hex", i, Case.UPPER, "hex" + "0".repeat(i - 3) + "100");
        }
    }

    private static void checkHexString(long value, String prefix, int zeroPad, Case hexCase, String expectedString) {

        checkHexString(null, value, prefix, zeroPad, hexCase, expectedString);

        final String prepended = "prepended".repeat(10);

        checkHexString(prepended, value, prefix, zeroPad, hexCase, prepended + expectedString);
    }

    private static void checkHexString(String prepend, long value, String prefix, int zeroPad, Case hexCase, String expectedString) {

        final StringBuilder sb = new StringBuilder();

        if (prepend != null) {

            sb.append(prepend);
        }

        StringBuilders.hexString(sb, value, prefix, zeroPad, hexCase);

        assertThat(sb.toString()).isEqualTo(expectedString);
    }
}
