package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntMaxDistanceNonBucketSetTest extends BaseMutableIntegerSetTest<MutableIntMaxDistanceNonBucketSet> {

    @Override
    MutableIntMaxDistanceNonBucketSet createSet(int initialCapacityExponent) {

        return new HeapMutableIntMaxDistanceNonBucketSet(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableIntMaxDistanceNonBucketSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach);
    }

    @Override
    boolean contains(MutableIntMaxDistanceNonBucketSet set, int value) {

        return set.contains(value);
    }

    @Override
    void addInAnyOrder(MutableIntMaxDistanceNonBucketSet set, int value) {

        set.addInAnyOrder(value);
    }

    @Override
    void addUnordered(MutableIntMaxDistanceNonBucketSet set, int value) {

        set.addUnordered(value);
    }

    @Override
    boolean addToSet(MutableIntMaxDistanceNonBucketSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableIntMaxDistanceNonBucketSet set, int value) {

        return set.removeAtMostOne(value);
    }
}
