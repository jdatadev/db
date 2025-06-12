package dev.jdata.db.utils.adt.strings;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class CharactersTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testAreEqualCaseInsensitive() {

        for (int i = 0; i <= 127; ++ i) {

            for (int j = 0; j <= 127; ++ j) {

                final char c1 = (char)i;
                final char c2 = (char)j;

                if (Character.isAlphabetic(c1) && Character.isAlphabetic(c2)) {

                    if (c1 == c2) {

                        final char c = c1;

                        checkAreEqualCaseInsensitive(c, c, true);

                        if (Character.isUpperCase(c)) {

                            checkAreEqualCaseInsensitive(c, Character.toLowerCase(c), true);
                            checkAreEqualCaseInsensitive(Character.toLowerCase(c), c, true);
                        }
                        else {
                            checkAreEqualCaseInsensitive(c, Character.toUpperCase(c), true);
                            checkAreEqualCaseInsensitive(Character.toUpperCase(c), c, true);
                        }
                    }
                    else if (Character.toLowerCase(c1) == Character.toLowerCase(c2)) {

                        checkAreEqualCaseInsensitive(c1, c2, true);
                    }
                    else {
                        checkAreEqualCaseInsensitive(c1, c2, false);
                    }
                }
                else {
                    assertThatThrownBy(() -> Characters.areASCIIEqualCaseInsensitive(c1, c2)).isInstanceOf(IllegalArgumentException.class);
                }
            }
        }
    }

    private void checkAreEqualCaseInsensitive(char c1, char c2, boolean expectedResult) {

        assertThat(Characters.areASCIIEqualCaseInsensitive(c1, c2)).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public void testIsPrintable() {

        for (int i = 0; i <= 127; ++ i) {

            final char c = (char)i;

            final boolean isPrintable = i >= 32 && i <= 126;

            assertThat(Characters.isPrintable(c)).isEqualTo(isPrintable);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testIsASCIIAlpha() {

        for (int i = 0; i <= 127; ++ i) {

            final char c = (char)i;

            final boolean isASCII = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');

            assertThat(Characters.isASCIIAlpha(c)).isEqualTo(isASCII);
        }
    }
}