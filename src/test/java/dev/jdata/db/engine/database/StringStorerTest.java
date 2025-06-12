package dev.jdata.db.engine.database;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.io.strings.StringResolver.CharacterBuffer;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;

import dev.jdata.db.utils.adt.arrays.BaseLargeCharArrayAndStringStorerTest;
import dev.jdata.db.utils.allocators.CharacterBuffersAllocator;
import dev.jdata.db.utils.function.BiObjToLongFunction;
import dev.jdata.db.utils.function.CharPredicate;

public final class StringStorerTest extends BaseLargeCharArrayAndStringStorerTest<StringStorer> {

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

        final ICharactersBufferAllocator charactersBufferAllocator = new CharacterBuffersAllocator();
        final StringBuilder sb = new StringBuilder();

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

                final long stringRef = expectedCharArrayIndex;

                assertThat(stringStorer.asString(stringRef)).isEqualTo(stringToAdd);

                stringStorer.makeString(stringRef, sb, charactersBufferAllocator, (b, n, s) -> {

                    for (int characterIndex = 0; characterIndex < n; ++ characterIndex) {

                        s.append(b[characterIndex]);
                    }

                    return null;
                });

                assertThat(sb.toString()).isEqualTo(stringToAdd);

                sb.setLength(0);

                expectedCharArrayIndex += stringToAdd.length() + 1;
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testToString() {

        final StringStorer stringStorer = create();

        final long stringRef1 = stringStorer.getOrAddStringRef("abc");
        final long stringRef2 = stringStorer.getOrAddStringRef("bcd");
        final long stringRef3 = stringStorer.getOrAddStringRef("cde");

        assertThat(stringRef1).isEqualTo(0L);
        assertThat(stringRef2).isEqualTo(4L);
        assertThat(stringRef3).isEqualTo(8L);

        final String expectedToString = stringStorer.getClass().getSimpleName() + " {" + stringRef3 +  "=cde," + stringRef2 + "=bcd," + stringRef1 + "=abc}";

        assertThat(stringStorer.toString()).isEqualTo(expectedToString);
    }

    @Override
    protected StringStorer create() {

        return new StringStorer(1, 0);
    }

    @Override
    protected boolean isEmpty(StringStorer instance) {

        return instance.isEmpty();
    }

    @Override
    protected long add(StringStorer instance, CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        return instance.getOrAddStringRef(characterBuffers, numCharacterBuffers);
    }

    @Override
    protected long add(StringStorer instance, String string) {

        return instance.getOrAddStringRef(string);
    }

    @Override
    protected long add(StringStorer instance, CharSequence charSequence, int offset, int length) {

        return instance.getOrAddStringRef(charSequence, offset, length);
    }

    @Override
    protected String getString(StringStorer instance, long stringRef) {

        return instance.asString(stringRef);
    }

    @Override
    protected boolean containsOnly(StringStorer instance, long stringRef, CharPredicate predicate) {

        return instance.containsOnly(stringRef, predicate);
    }

    @Override
    protected boolean equals(StringStorer instance1, long stringRef1, StringStorer instance2, long stringRef2) {

        return instance1.equals(stringRef1, instance2, stringRef2);
    }

    @Override
    protected boolean equals(StringStorer instance1, long stringRef1, StringStorer instance2, long stringRef2, boolean caseSensitive) {

        return instance1.equals(stringRef1, instance2, stringRef2, caseSensitive);
    }
}
