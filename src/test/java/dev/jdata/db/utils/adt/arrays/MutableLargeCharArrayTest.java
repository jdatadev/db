package dev.jdata.db.utils.adt.arrays;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.function.CharPredicate;

public final class MutableLargeCharArrayTest extends BaseLargeCharArrayAndStringStorerTest<MutableStringsCharLargeArray> {

    @Override
    protected MutableStringsCharLargeArray create() {

        return HeapMutableStringsCharLargeArray.create(AllocationType.HEAP, 1, 0);
    }

    @Override
    protected boolean isEmpty(MutableStringsCharLargeArray instance) {

        return instance.isEmpty();
    }

    @Override
    protected long add(MutableStringsCharLargeArray instance, CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        final long result = instance.getLimit();

        instance.add(characterBuffers, numCharacterBuffers);

        return result;
    }

    @Override
    protected long add(MutableStringsCharLargeArray instance, String string) {

        final long result = instance.getLimit();

        instance.add(string, 0, string.length());

        return result;
    }

    @Override
    protected long add(MutableStringsCharLargeArray instance, CharSequence charSequence, int offset, int length) {

        final long result = instance.getLimit();

        instance.add(charSequence, offset, length);

        return result;
    }

    @Override
    protected String getString(MutableStringsCharLargeArray instance, long stringRef) {

        return instance.asString(stringRef);
    }

    @Override
    protected boolean containsOnly(MutableStringsCharLargeArray instance, long stringRef, CharPredicate predicate) {

        return instance.containsOnlyCharacters(stringRef, predicate);
    }

    @Override
    protected boolean equals(MutableStringsCharLargeArray instance1, long stringRef1, MutableStringsCharLargeArray instance2, long stringRef2) {

        return instance1.equals(stringRef1, instance2, stringRef2);
    }

    @Override
    protected boolean equals(MutableStringsCharLargeArray instance1, long stringRef1, MutableStringsCharLargeArray instance2, long stringRef2, boolean caseSensitive) {

        return instance1.equals(stringRef1, instance2, stringRef2, caseSensitive);
    }
}
