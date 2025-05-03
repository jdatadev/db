package dev.jdata.db.engine.database;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.function.BiObjToLongFunction;

public final class StringStorerTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testStore() {

        checkStore(StringStorer::getOrAddStringRef);
    }

    @Test
    @Category(UnitTest.class)
    public void testStoreWithLength() {

        checkStore((stringStorer, string) -> stringStorer.getOrAddStringRef(string, 0, string.length()));
    }

    @Test
    @Category(UnitTest.class)
    public void testStoreWithPrefixSuffix() {

        final String prefix = "prefix";
        final String suffix = "suf";

        final int prefixLength = prefix.length();

        checkStore((stringStorer, string) -> stringStorer.getOrAddStringRef(prefix + string + suffix, prefixLength, string.length()));
    }

    private void checkStore(BiObjToLongFunction<StringStorer, String> getOrAddStringRef) {

        for (int initialCapacityExponent = 0; initialCapacityExponent < 8; ++ initialCapacityExponent) {

            final StringStorer stringStorer = new StringStorer(1, initialCapacityExponent);

            final int numToAdd = 1000;

            long expectedCharArrayIndex = 0L;

            for (int i = 0; i < numToAdd; ++ i) {

                final String stringToAdd = "toAdd" + i;

                assertThat(stringStorer.contains(stringToAdd)).isFalse();

                assertThat(getOrAddStringRef.apply(stringStorer, stringToAdd)).isEqualTo(expectedCharArrayIndex);
                assertThat(getOrAddStringRef.apply(stringStorer, stringToAdd)).isEqualTo(expectedCharArrayIndex);
                assertThat(getOrAddStringRef.apply(stringStorer, stringToAdd)).isEqualTo(expectedCharArrayIndex);

                assertThat(stringStorer.contains(stringToAdd)).isTrue();

                expectedCharArrayIndex += stringToAdd.length() + 1;
            }
        }
    }
}
