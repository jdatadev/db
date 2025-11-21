package dev.jdata.db.engine.database.strings;

import java.util.Objects;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.engine.database.strings.IStringCache;
import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

abstract class BaseStringCacheTest<T extends IStringCache> extends BaseTest {

    abstract T createStringCache(int initialOuterCapacityExponent, int innerCapacityExponent);

    @Test
    @Category(UnitTest.class)
    public final void testGetOrAddString() {

        final T stringCache = createStringCache();

        assertThatThrownBy(() -> stringCache.getOrAddString(null)).isInstanceOf(NullPointerException.class);

        checkGetOrAddString(stringCache, "");
        checkGetOrAddString(stringCache, " ");
        checkGetOrAddString(stringCache, "a");
        checkGetOrAddString(stringCache, " a ");
        checkGetOrAddString(stringCache, "abc");
        checkGetOrAddString(stringCache, " abc ");
    }

    private void checkGetOrAddString(T stringCache, String string) {

        Objects.requireNonNull(stringCache);
        Objects.requireNonNull(string);

        final T stringTestStringCache = createStringCache();

        assertThat(stringTestStringCache.getOrAddString(string)).isSameAs(string);
        assertThat(stringTestStringCache.getOrAddString(string)).isSameAs(string);

        checkGetOrAddCharSequence(stringCache, string);
    }

    private void checkGetOrAddCharSequence(T stringCache, CharSequence charSequence) {

        Objects.requireNonNull(stringCache);
        Objects.requireNonNull(charSequence);

        final String expectedString = Strings.of(charSequence);

        String initialString;

        assertThat((initialString = stringCache.getOrAddString(charSequence))).isEqualTo(expectedString);
        assertThat(stringCache.getOrAddString(Strings.of(charSequence))).isSameAs(initialString);
        assertThat(stringCache.getOrAddString(new StringBuilder(charSequence))).isSameAs(initialString);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetOrAddStringWithStartIndexAndNumCharacters() {

        final T stringCache = createStringCache();

        assertThatThrownBy(() -> stringCache.getOrAddString(null, 0, 1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> stringCache.getOrAddString("abc", -1, 1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringCache.getOrAddString("abc", 3, 1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringCache.getOrAddString("abc", 0, -1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringCache.getOrAddString("abc", 0, 4)).isInstanceOf(IndexOutOfBoundsException.class);

        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "", 0, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " ", 0, 1, " ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "a", 0, 1, "a");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " a ", 0, 3, " a ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 0, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 0, 1, "a");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 0, 2, "ab");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 0, 3, "abc");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 1, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 1, 1, "b");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 1, 2, "bc");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 2, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 2, 1, "c");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, "abc", 3, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 0, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 0, 1, " ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 0, 2, " a");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 0, 3, " ab");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 0, 4, " abc");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 0, 5, " abc ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 1, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 1, 1, "a");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 1, 2, "ab");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 1, 3, "abc");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 1, 4, "abc ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 2, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 2, 1, "b");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 2, 2, "bc");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 2, 3, "bc ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 3, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 3, 1, "c");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 3, 2, "c ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 4, 0, "");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 4, 1, " ");
        checkGetOrAddStringWithStartIndexAndNumCharacters(stringCache, " abc ", 5, 0, "");
    }

    private void checkGetOrAddStringWithStartIndexAndNumCharacters(T stringCache, String string, int startIndex, int numCharacters, String expectedString) {

        Objects.requireNonNull(stringCache);
        Objects.requireNonNull(string);

        final T stringTestStringCache = createStringCache();

        assertThat(stringTestStringCache.getOrAddString(string, 0, string.length())).isSameAs(string);
        assertThat(stringTestStringCache.getOrAddString(string, 0, string.length())).isSameAs(string);

        checkGetOrAddCharSequence(stringCache, string, startIndex, numCharacters);
    }

    private void checkGetOrAddCharSequence(T stringCache, CharSequence charSequence, int startIndex, int numCharacters) {

        Objects.requireNonNull(stringCache);
        Objects.requireNonNull(charSequence);

        final String expectedString = Strings.of(charSequence, startIndex, numCharacters);

        String initialString;

        assertThat((initialString = stringCache.getOrAddString(charSequence, startIndex, numCharacters))).isEqualTo(expectedString);
        assertThat(stringCache.getOrAddString(Strings.of(charSequence, startIndex, numCharacters))).isSameAs(initialString);
        assertThat(stringCache.getOrAddString(new StringBuilder(charSequence).subSequence(startIndex, startIndex + numCharacters))).isSameAs(initialString);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetOrAddInt() {

        final T stringCache = createStringCache();

        assertThatThrownBy(() -> stringCache.getOrAddString(null)).isInstanceOf(NullPointerException.class);

        checkGetOrAddInt(stringCache, Integer.MIN_VALUE);
        checkGetOrAddInt(stringCache, -123);
        checkGetOrAddInt(stringCache, -1);
        checkGetOrAddInt(stringCache, 0);
        checkGetOrAddInt(stringCache, 1);
        checkGetOrAddInt(stringCache, 123);
        checkGetOrAddInt(stringCache, Integer.MAX_VALUE);
    }

    private void checkGetOrAddInt(T stringCache, int integer) {

        Objects.requireNonNull(stringCache);

        final String expectedString = String.valueOf(integer);

        String initialString;

        assertThat((initialString = stringCache.getOrAddString(integer))).isEqualTo(expectedString);
        assertThat(stringCache.getOrAddString(integer)).isSameAs(initialString);
    }

    private T createStringCache() {

        return createStringCache(0, 0);
    }
}
