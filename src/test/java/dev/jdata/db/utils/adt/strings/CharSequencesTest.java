package dev.jdata.db.utils.adt.strings;

import java.util.function.Function;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class CharSequencesTest extends BaseCharSequencesTest {

    @Test
    @Category(UnitTest.class)
    public void testStartsWith() {

        checkStartsWith(String::toString);
        checkStartsWith(StringBuilder::new);
    }

    private void checkStartsWith(Function<String, CharSequence> createCharSequence) {

        assertThatThrownBy(() -> CharSequences.startsWith(null, createCharSequence.apply("abc"))).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> CharSequences.startsWith(createCharSequence.apply("abc"), null)).isInstanceOf(NullPointerException.class);

        checkStartsWith("", "", createCharSequence, false);
        checkStartsWith("a", "", createCharSequence, false);
        checkStartsWith("", "a", createCharSequence, false);
        checkStartsWith("a", "a", createCharSequence, true);
        checkStartsWith("a", "ab", createCharSequence, false);
        checkStartsWith("ab", "a", createCharSequence, true);
        checkStartsWith("a", "b", createCharSequence, false);
        checkStartsWith("ab", "ac", createCharSequence, false);
        checkStartsWith("abc", "a", createCharSequence, true);
        checkStartsWith("abc", "ab", createCharSequence, true);
        checkStartsWith("abc", "abc", createCharSequence, true);
        checkStartsWith("abc", "abcd", createCharSequence, false);
    }

    private void checkStartsWith(String charSequenceString, String otherString, Function<String, CharSequence> createCharSequence, boolean expectedResult) {

        final CharSequence charSequence = createCharSequence.apply(charSequenceString);
        final CharSequence otherCharSequence = createCharSequence.apply(otherString);

        assertThat(CharSequences.startsWith(charSequence, otherCharSequence)).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public void testHasFirstCharacterAndRemaining() {

        checkHasFirstCharacterAndRemaining("", 0, false);
        checkHasFirstCharacterAndRemaining("a", 0, false);
        checkHasFirstCharacterAndRemaining("a", 1, true);
        checkHasFirstCharacterAndRemaining("0", 1, false);
        checkHasFirstCharacterAndRemaining("9", 1, false);
        checkHasFirstCharacterAndRemaining("a1", 2, true);
        checkHasFirstCharacterAndRemaining("a ", 1, true);
        checkHasFirstCharacterAndRemaining("a ", 2, false);
        checkHasFirstCharacterAndRemaining("abc ", 4, false);
    }

    private void checkHasFirstCharacterAndRemaining(String charSequenceString, int numCharaters, boolean expectedResult) {

        assertThat(CharSequences.hasFirstCharacterAndRemaining(charSequenceString, numCharaters, Character::isAlphabetic,
                c -> Character.isAlphabetic(c) || Character.isDigit(c)))
            .isEqualTo(expectedResult);
    }
}
