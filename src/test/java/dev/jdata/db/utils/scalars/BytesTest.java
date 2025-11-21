package dev.jdata.db.utils.scalars;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class BytesTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testToHexUnsigned() {

        assertThatThrownBy(() -> Bytes.toHexUnsigned((byte)0, null)).isInstanceOf(NullPointerException.class);

        checkToHexUnsigned(Byte.MIN_VALUE, "80");
        checkToHexUnsigned((byte)-1, "FF");
        checkToHexUnsigned((byte)0, "00");
        checkToHexUnsigned((byte)9, "09");
        checkToHexUnsigned((byte)10, "0A");
        checkToHexUnsigned((byte)16, "10");
        checkToHexUnsigned(Byte.MAX_VALUE, "7F");

        for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; ++ b) {

            final String expectedString = Integer.toHexString(Byte.toUnsignedInt(b)).toUpperCase();
pad
            checkToHexUnsigned(b, expectedString.length() == 1 ? '0' + expectedString : expectedString);
        }
    }

    private static void checkToHexUnsigned(byte b, String expectedString) {

        final StringBuilder sb = new StringBuilder(expectedString.length());

        Bytes.toHexUnsigned(b, sb);

        assertThatCharSeq(sb).isEqualToCharSequence(expectedString);
    }
}
