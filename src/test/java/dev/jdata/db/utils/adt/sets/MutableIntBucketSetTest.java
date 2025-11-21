package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntBucketSetTest extends BaseMutableIntegerSetTest<MutableIntBucketSet> {

    @Override
    MutableIntBucketSet createSet(int initialCapacityExponent) {

        return new HeapMutableIntBucketSet(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableIntBucketSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach);
    }

    @Override
    boolean contains(MutableIntBucketSet set, int value) {

        return set.contains(value);
    }

    @Override
    void addInAnyOrder(MutableIntBucketSet set, int value) {

        set.addInAnyOrder(value);
    }

    @Override
    void addUnordered(MutableIntBucketSet set, int value) {

        set.addUnordered(value);
    }

    @Override
    boolean addToSet(MutableIntBucketSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableIntBucketSet set, int value) {

        return set.removeAtMostOne(value);
    }
}
