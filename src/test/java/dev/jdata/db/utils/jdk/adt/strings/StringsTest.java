package dev.jdata.db.utils.jdk.adt.strings;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.function.CharPredicate;

public final class StringsTest extends BaseCharSequencesTest {

    @Test
    @Category(UnitTest.class)
    public void testAllStringUtilityMethods() {

        throw new UnsupportedOperationException();
    }

    @Override
    boolean isASCIIAlphaNumeric(String string) {

        return Strings.isASCIIAlphaNumeric(string);
    }

    @Override
    boolean isASCIIAlphaNumeric(String string, CharPredicate additionalPredicate) {

        return Strings.isASCIIAlphaNumeric(string, additionalPredicate);
    }

    @Override
    boolean containsAny(String string, CharPredicate predicate) {

        return Strings.containsAny(string, predicate);
    }

    @Override
    boolean containsAny(String string, int startIndex, int numCharacters, CharPredicate predicate) {

        return Strings.containsAny(string, startIndex, numCharacters, predicate);
    }
}
