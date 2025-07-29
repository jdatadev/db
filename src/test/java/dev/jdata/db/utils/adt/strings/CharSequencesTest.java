package dev.jdata.db.utils.adt.strings;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharPredicate;
import dev.jdata.db.utils.scalars.Integers;

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

    @Test
    @Category(AdhocTest.class)
    public void testCharSequenceHash() {

        final int[] percentages = new int[] { 10, 25, 50, 75, 100 };

        final Random random = new Random(1L);

        for (int capacityExponent = 0; capacityExponent < 24; ++ capacityExponent) {

            for (int percentage : percentages) {

                final long now = System.currentTimeMillis();

                final int numCollisions = checkCharSequenceHash(capacityExponent, percentage, b -> generateRandomString(b, random));

                final int numCollisionsPercent = (numCollisions * 100) / CapacityExponents.computeIntCapacityFromExponent(capacityExponent);

                final long time = System.currentTimeMillis() - now;

                System.out.println("test char sequence hash capacityExponent=" + capacityExponent + " numCollisions=" + numCollisions
                        + " numCollisionsPercent=" + numCollisionsPercent + " time=" + time + " percentage=" + percentage);
            }
        }
    }

    private static void generateRandomString(StringBuilder sb, Random random) {

        final int length = random.nextInt(30);

        for (int j = 0; j < length; ++ j) {

            final int codePointOffset = random.nextInt(127 - ' ');

            final char c = (char)(' ' + codePointOffset);

            sb.append(c);
        }
    }

    private int checkCharSequenceHash(int capacityExponent, int capacityPercentage, Consumer<StringBuilder> generateKey) {

        Checks.isLessThan(capacityExponent, Integer.SIZE - 1);

        final int capacity = 1 << capacityExponent;
        final int keyMask = CapacityExponents.makeIntKeyMask(capacityExponent);

        final long[] keys = new long[capacity];
        final StringBuilder sb = new StringBuilder();

        int numCollisions = 0;

        final int numElements = (capacity * capacityPercentage) / 100;

        for (int i = 0; i < numElements; ++ i) {

            generateKey.accept(sb);

            final long hash = CharSequences.longHashCode(sb, capacityExponent);

            sb.setLength(0);

            final long longHashArrayIndex = HashFunctions.longHashArrayIndex(hash, keyMask);

            final int hashArrayIndex = Integers.checkUnsignedLongToUnsignedInt(longHashArrayIndex);

            if (keys[hashArrayIndex] > 0) {

                ++ numCollisions;
            }

            ++ keys[hashArrayIndex];
        }

        return numCollisions;
    }

    @Override
    boolean isASCIIAlphaNumeric(String string) {

        return CharSequences.isASCIIAlphaNumeric(string);
    }

    @Override
    boolean isASCIIAlphaNumeric(String string, CharPredicate additionalPredicate) {

        return CharSequences.isASCIIAlphaNumeric(string, additionalPredicate);
    }

    @Override
    boolean containsAny(String string, CharPredicate predicate) {

        return CharSequences.containsAny(string, predicate);
    }

    @Override
    boolean containsAny(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        return CharSequences.containsAny(string, startIndex, numCharacters, predicate);
    }
}
