package dev.jdata.db.utils.jdk.adt.strings;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.function.CharPredicate;

public abstract class BaseCharSequencesTest extends BaseTest {

    abstract boolean isASCIIAlphaNumeric(String string);
    abstract boolean isASCIIAlphaNumeric(String string, CharPredicate additionalPredicate);

    abstract boolean containsAny(String string, CharPredicate predicate);
    abstract boolean containsAny(String string, int startIndex, int numCharacters, CharPredicate predicate);

    @Test
    @Category(UnitTest.class)
    public final void testIsASCIIAlphaNumeric() {

        assertThatThrownBy(() -> isASCIIAlphaNumeric(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> isASCIIAlphaNumeric("")).isInstanceOf(IllegalArgumentException.class);

        assertThat(isASCIIAlphaNumeric(" ")).isFalse();
        assertThat(isASCIIAlphaNumeric("abc123")).isTrue();
        assertThat(isASCIIAlphaNumeric(" abc123")).isFalse();
        assertThat(isASCIIAlphaNumeric("abc 123")).isFalse();
        assertThat(isASCIIAlphaNumeric("abc123 ")).isFalse();

        for (int i = 0; i <= 127; ++ i) {

            final char c = (char)i;

            assertThat(isASCIIAlphaNumeric(String.valueOf(c))).isEqualTo((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsASCIIAlphaNumericWithAdditionalPredicate() {

        assertThatThrownBy(() -> isASCIIAlphaNumeric(null, c -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> isASCIIAlphaNumeric("", c -> true)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> isASCIIAlphaNumeric("abc", null)).isInstanceOf(NullPointerException.class);

        assertThat(isASCIIAlphaNumeric(" ", c -> true)).isTrue();
        assertThat(isASCIIAlphaNumeric(" ", c -> false)).isFalse();

        assertThat(isASCIIAlphaNumeric("abc123", c -> true)).isTrue();
        assertThat(isASCIIAlphaNumeric("abc123", c -> false)).isTrue();

        assertThat(isASCIIAlphaNumeric(" abc123", c -> true)).isTrue();
        assertThat(isASCIIAlphaNumeric("abc 123", c -> true)).isTrue();
        assertThat(isASCIIAlphaNumeric("abc123 ", c -> true)).isTrue();

        assertThat(isASCIIAlphaNumeric(" abc123", c -> false)).isFalse();
        assertThat(isASCIIAlphaNumeric("abc 123", c -> false)).isFalse();
        assertThat(isASCIIAlphaNumeric("abc123 ", c -> false)).isFalse();

        assertThat(isASCIIAlphaNumeric(" abc123", c -> c == ' ')).isTrue();
        assertThat(isASCIIAlphaNumeric("abc 123", c -> c == ' ')).isTrue();
        assertThat(isASCIIAlphaNumeric("abc123 ", c -> c == ' ')).isTrue();

        for (int i = 0; i <= 127; ++ i) {

            final char c = (char)i;

            assertThat(isASCIIAlphaNumeric(String.valueOf(c), character -> false)).isEqualTo((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsAny() {

        assertThatThrownBy(() -> containsAny(null, c -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> containsAny("abc", null)).isInstanceOf(NullPointerException.class);

        assertThat(containsAny("", c -> true)).isFalse();
        assertThat(containsAny(" ", c -> true)).isTrue();

        assertThat(containsAny("abc", c -> c == 'a')).isTrue();
        assertThat(containsAny("abc", c -> c == 'b')).isTrue();
        assertThat(containsAny("abc", c -> c == 'c')).isTrue();
        assertThat(containsAny("abc", c -> c == 'd')).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsAnyWithIndex() {

        assertThatThrownBy(() -> containsAny(null, c -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> containsAny("abc", -1, 1, c -> true)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> containsAny("abc", 3, 0, c -> true)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> containsAny("abc", 0, -1, c -> true)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> containsAny("abc", 0, 4, c -> true)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> containsAny("abc", 1, 3, c -> true)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> containsAny("abc", 0, 1, null)).isInstanceOf(NullPointerException.class);

        assertThat(containsAny("", 0, 0, c -> true)).isFalse();
        assertThat(containsAny(" ", 0, 0, c -> true)).isFalse();
        assertThat(containsAny(" ", 0, 1, c -> true)).isTrue();

        assertThat(containsAny("abc", 0, 3, c -> c == 'a')).isTrue();
        assertThat(containsAny("abc", 0, 0, c -> c == 'a')).isFalse();
        assertThat(containsAny("abc", 1, 2, c -> c == 'a')).isFalse();
        assertThat(containsAny("abc", 2, 1, c -> c == 'a')).isFalse();

        assertThat(containsAny("abc", 0, 3, c -> c == 'b')).isTrue();
        assertThat(containsAny("abc", 1, 0, c -> c == 'b')).isFalse();
        assertThat(containsAny("abc", 1, 2, c -> c == 'b')).isTrue();
        assertThat(containsAny("abc", 2, 1, c -> c == 'b')).isFalse();

        assertThat(containsAny("abc", 0, 3, c -> c == 'c')).isTrue();
        assertThat(containsAny("abc", 1, 0, c -> c == 'c')).isFalse();
        assertThat(containsAny("abc", 1, 2, c -> c == 'c')).isTrue();
        assertThat(containsAny("abc", 2, 1, c -> c == 'c')).isTrue();

        assertThat(containsAny("abc", 0, 3, c -> c == 'd')).isFalse();
        assertThat(containsAny("abc", 1, 0, c -> c == 'd')).isFalse();
        assertThat(containsAny("abc", 1, 2, c -> c == 'd')).isFalse();
        assertThat(containsAny("abc", 2, 1, c -> c == 'd')).isFalse();
    }
}
