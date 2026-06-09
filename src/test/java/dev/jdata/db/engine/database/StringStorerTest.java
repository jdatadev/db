package dev.jdata.db.engine.database;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.jutils.io.strings.StringResolver.CharacterBuffer;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;

import dev.jdata.db.utils.adt.arrays.BaseLargeCharArrayAndStringStorerTest;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
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

    private static void checkStore(BiObjToLongFunction<StringStorer, String> getOrAddStringRef) {

        final ICharactersBufferAllocator charactersBufferAllocator = new CharacterBuffersAllocator();
        final StringBuilder sb = new StringBuilder();

        StringStoreTestUtil.checkStore((i, o) -> new StringStorer(AllocationType.HEAP, i, o), StringStorer::contains, StringStorer::getOrAddStringRef,
                (getOrAddResult, expectedCharArrayIndex) ->
        {
            if (getOrAddResult != expectedCharArrayIndex) {

                assertThat(getOrAddResult).isEqualTo(expectedCharArrayIndex);
            }
        },
        (stringStore, stringToAdd, expectedCharArrayIndex) -> {

            final long stringRef = expectedCharArrayIndex;

            assertThat(stringStore.asString(stringRef)).isEqualTo(stringToAdd);

            stringStore.makeString(stringRef, sb, charactersBufferAllocator, (b, n, s) -> {

                for (int characterIndex = 0; characterIndex < n; ++ characterIndex) {

                    s.append(b[characterIndex]);
                }

                return null;
            });

            assertThat(sb.toString()).isEqualTo(stringToAdd);

            sb.setLength(0);
        });
    }

    @Test
    @Category(UnitTest.class)
    public void testCharAt() {

        final StringStorer stringStorer = create();

        final long stringRef1 = stringStorer.getOrAddStringRef("abc");
        final long stringRef2 = stringStorer.getOrAddStringRef("def");
        final long stringRef3 = stringStorer.getOrAddStringRef("ghi");

        assertThatThrownBy(() -> stringStorer.charAt(-1L, 0L)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> stringStorer.charAt(0L, -1L)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringStorer.charAt(0L, 3L)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringStorer.charAt(3L, 0L)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringStorer.charAt(7L, 0L)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringStorer.charAt(11L, 0L)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> stringStorer.charAt(12L, 0L)).isInstanceOf(IllegalArgumentException.class);

        assertThat(stringStorer.charAt(0L, 0L)).isEqualTo('a');
        assertThat(stringStorer.charAt(0L, 1L)).isEqualTo('b');
        assertThat(stringStorer.charAt(0L, 2L)).isEqualTo('c');

        assertThat(stringStorer.charAt(1L, 0L)).isEqualTo('b');
        assertThat(stringStorer.charAt(1L, 1L)).isEqualTo('c');

        assertThat(stringStorer.charAt(2L, 0L)).isEqualTo('c');

        assertThat(stringStorer.charAt(4L, 0L)).isEqualTo('d');
        assertThat(stringStorer.charAt(4L, 1L)).isEqualTo('e');
        assertThat(stringStorer.charAt(4L, 2L)).isEqualTo('f');

        assertThat(stringStorer.charAt(5L, 0L)).isEqualTo('e');
        assertThat(stringStorer.charAt(5L, 1L)).isEqualTo('f');

        assertThat(stringStorer.charAt(6L, 0L)).isEqualTo('f');

        assertThat(stringStorer.charAt(8L, 0L)).isEqualTo('g');
        assertThat(stringStorer.charAt(8L, 1L)).isEqualTo('h');
        assertThat(stringStorer.charAt(8L, 2L)).isEqualTo('i');

        assertThat(stringStorer.charAt(9L, 0L)).isEqualTo('h');
        assertThat(stringStorer.charAt(9L, 1L)).isEqualTo('i');

        assertThat(stringStorer.charAt(10L, 0L)).isEqualTo('i');
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

        final String string = stringStorer.toString();

        assertThat(string.startsWith(stringStorer.getClass().getSimpleName() + " {"));
        assertThat(string).contains("=abc", "=bcd", "=cde");
    }

    @Override
    protected StringStorer create() {

        return new StringStorer(AllocationType.HEAP, 0, 0);
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
