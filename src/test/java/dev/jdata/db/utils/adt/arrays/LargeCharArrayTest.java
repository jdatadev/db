package dev.jdata.db.utils.adt.arrays;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

import dev.jdata.db.utils.function.CharPredicate;

public final class LargeCharArrayTest extends BaseLargeCharArrayAndStringStorerTest<LargeCharArray> {

    @Override
    protected LargeCharArray create() {

        return new LargeCharArray(1, 0);
    }

    @Override
    protected boolean isEmpty(LargeCharArray instance) {

        return instance.isEmpty();
    }

    @Override
    protected long add(LargeCharArray instance, CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        final long result = instance.getLimit();

        instance.add(characterBuffers, numCharacterBuffers);

        return result;
    }

    @Override
    protected long add(LargeCharArray instance, String string) {

        final long result = instance.getLimit();

        instance.add(string, 0, string.length());

        return result;
    }

    @Override
    protected long add(LargeCharArray instance, CharSequence charSequence, int offset, int length) {

        final long result = instance.getLimit();

        instance.add(charSequence, offset, length);

        return result;
    }

    @Override
    protected String getString(LargeCharArray instance, long stringRef) {

        return instance.asString(stringRef);
    }

    @Override
    protected boolean containsOnly(LargeCharArray instance, long stringRef, CharPredicate predicate) {

        return instance.containsOnly(stringRef, predicate);
    }

    @Override
    protected boolean equals(LargeCharArray instance1, long stringRef1, LargeCharArray instance2, long stringRef2) {

        return instance1.equals(stringRef1, instance2, stringRef2);
    }

    @Override
    protected boolean equals(LargeCharArray instance1, long stringRef1, LargeCharArray instance2, long stringRef2, boolean caseSensitive) {

        return instance1.equals(stringRef1, instance2, stringRef2, caseSensitive);
    }
}
