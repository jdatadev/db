package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver.CharacterBuffer;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.function.CharPredicate;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

public abstract class BaseLargeCharArrayAndStringStorerTest<T> extends BaseTest {

    protected abstract T create();

    protected abstract boolean isEmpty(T instance);

    protected abstract long add(T instance, CharacterBuffer[] characterBuffers, int numCharacterBuffers);
    protected abstract long add(T instance, String string);
    protected abstract long add(T instance, CharSequence charSequence, int offset, int length);

    protected abstract String getString(T instance, long stringRef);

    protected abstract boolean containsOnly(T instance, long stringRef, CharPredicate predicate);

    protected abstract boolean equals(T instance1, long stringRef1, T instance2, long stringRef2);
    protected abstract boolean equals(T instance1, long stringRef1, T instance2, long stringRef2, boolean caseSensitive);

    @Test
    @Category(UnitTest.class)
    public final void testStoreThrowsExceptionForEmptyStrings() {

        checkAddThrowsExceptionForEmptyStrings((i, s) -> {

            final CharacterBuffer characterBuffer = new CharacterBuffer();

            characterBuffer.initializeWithoutChecks(s.toCharArray(), 0, s.length());

            return add(i, new CharacterBuffer[] { characterBuffer }, 1);
        });

        checkAddThrowsExceptionForEmptyStrings((i, s) -> {

            final CharacterBuffer characterBuffer1 = new CharacterBuffer();
            final CharacterBuffer characterBuffer2 = new CharacterBuffer();

            characterBuffer1.initializeWithoutChecks(new char[0], 0, 0);
            characterBuffer2.initializeWithoutChecks(s.toCharArray(), 0, s.length());

            return add(i, new CharacterBuffer[] { characterBuffer1, characterBuffer2, }, 2);
        });

        checkAddThrowsExceptionForEmptyStrings((i, s) -> add(i, s));

        checkAddThrowsExceptionForEmptyStrings((i, s) -> add(i, s, 0, s.length()));
    }

    private interface StringAdder<T> {

        long addString(T instance, String string);
    }

    private void checkAddThrowsExceptionForEmptyStrings(StringAdder<T> stringAdder) {

        final T instance = create();

        assertThatThrownBy(() -> stringAdder.addString(instance, "")).isInstanceOf(IllegalArgumentException.class);
        assertThat(isEmpty(instance)).isTrue();

        checkAddString(instance, " ", stringAdder);
        checkAddString(instance, "a", stringAdder);
        checkAddString(instance, "abc", stringAdder);
        checkAddString(instance, " abc ", stringAdder);
    }

    private void checkAddString(T instance, String string, StringAdder<T> stringAdder) {

        final long stringRef = stringAdder.addString(instance, string);

        assertThat(getString(instance, stringRef)).isEqualTo(string);
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsOnly() {

        final T instance = create();

        final long stringRef = add(instance, "abc");

        assertThatThrownBy(() -> containsOnly(instance, StringRef.STRING_NONE, Character::isWhitespace)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> containsOnly(instance, stringRef, null)).isInstanceOf(NullPointerException.class);

        checkContainsOnly(" ", Character::isWhitespace, true);
        checkContainsOnly("abc", Character::isLowerCase, true);
        checkContainsOnly("abc", Character::isLowerCase, true);

        final int hundredThousand = 100 * 1000;

        checkContainsOnly(Strings.repeat("abc", hundredThousand), Character::isLowerCase, true);
        checkContainsOnly(Strings.repeat("abc", hundredThousand).concat("aBc"), Character::isLowerCase, false);
    }

    private void checkContainsOnly(String string, CharPredicate charPredicate, boolean expectedResult) {

        final T instance = create();

        final long stringRef = add(instance, string);

        assertThat(containsOnly(instance, stringRef, charPredicate)).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public final void testEquals() {

        checkEquals(" ", " ", true);
        checkEquals(" ", "  ", false);
        checkEquals("ab", "abc", false);
        checkEquals("abc", "ab", false);
        checkEquals("abc", "abcd", false);
        checkEquals("abc", "abc", true);
        checkEquals("abc", "aBc", false);
        checkEquals("abc1", "abc1", true);
        checkEquals("abc1", "aBc1", false);
        checkEquals("abc1", "abc2", false);

        final int hundredThousand = 100 * 1000;

        checkEquals(Strings.repeat("abc", hundredThousand), Strings.repeat("abc", hundredThousand), true);
        checkEquals(Strings.repeat("abc", hundredThousand), Strings.repeat("abc", hundredThousand - 1), false);
    }

    private void checkEquals(String string1, String string2, boolean expectedResult) {

        final T instance1 = create();
        final T instance2 = create();

        final long stringRef1 = add(instance1, string1);
        final long stringRef2 = add(instance2, string2);

        assertThat(equals(instance1, stringRef1, instance2, stringRef2)).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public final void testEqualsCaseInsensitive() {

        checkEqualsCaseInsensitive(" ", " ", true);
        checkEqualsCaseInsensitive(" ", "  ", false);
        checkEqualsCaseInsensitive("ab", "abc", false);
        checkEqualsCaseInsensitive("abc", "ab", false);
        checkEqualsCaseInsensitive("abc", "abcd", false);
        checkEqualsCaseInsensitive("abc", "abc", true);
        checkEqualsCaseInsensitive("aB", "abc", false);
        checkEqualsCaseInsensitive("abc", "aBc", true);
        checkEqualsCaseInsensitive("abc1", "aBc1", true);
        checkEqualsCaseInsensitive("abc1", "aBc2", false);

        final int hundredThousand = 100 * 1000;

        checkEqualsCaseInsensitive(Strings.repeat("abc", hundredThousand), Strings.repeat("abc", hundredThousand), true);
        checkEqualsCaseInsensitive(Strings.repeat("abc", hundredThousand), Strings.repeat("abc", hundredThousand - 1), false);

        checkEqualsCaseInsensitive(Strings.repeat("abc", hundredThousand), Strings.repeat("aBc", hundredThousand), true);
        checkEqualsCaseInsensitive(Strings.repeat("abc", hundredThousand), Strings.repeat("aBc", hundredThousand - 1), false);
    }

    private void checkEqualsCaseInsensitive(String string1, String string2, boolean expectedResult) {

        final T instance1 = create();
        final T instance2 = create();

        final long stringRef1 = add(instance1, string1);
        final long stringRef2 = add(instance2, string2);

        assertThat(equals(instance1, stringRef1, instance2, stringRef2, false)).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public final void testEqualsWithIndex() {

        final T instance1 = create();
        final T instance2 = create();

        final String string = "abc";

        add(instance1, "preadded");

        final long stringRef1 = add(instance1, string);
        final long stringRef2 = add(instance2, string);

        assertThat(stringRef1).isNotSameAs(stringRef2);

        assertThat(equals(instance1, stringRef1, instance2, stringRef2)).isTrue();
    }
}
