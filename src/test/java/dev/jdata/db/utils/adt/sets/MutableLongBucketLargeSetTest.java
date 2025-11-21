package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongBucketLargeSetTest extends BaseMutableIntegerSetTest<MutableLongBucketLargeSet> {

    @Override
    MutableLongBucketLargeSet createSet(int initialCapacity) {

        return HeapMutableLongBucketLargeSet.create(AllocationType.HEAP, initialCapacity);
    }

    @Override
    <P> void forEach(MutableLongBucketLargeSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach != null ? (e, p) -> forEach.each(Integers.checkUnsignedLongToUnsignedInt(e), p) : null);
    }

    @Override
    void addInAnyOrder(MutableLongBucketLargeSet set, int value) {

        set.addInAnyOrder(value);
    }

    @Override
    void addUnordered(MutableLongBucketLargeSet set, int value) {

        set.addUnordered(value);
    }

    @Override
    boolean addToSet(MutableLongBucketLargeSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableLongBucketLargeSet set, int value) {

        return set.removeAtMostOne(value);
    }

    @Override
    boolean contains(MutableLongBucketLargeSet set, int value) {

        return set.contains(value);
    }
}
