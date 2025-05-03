package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntElements.ForEach;

public final class MutableIntMaxDistanceNonBucketSetTest extends BaseMutableIntegerSetTest<MutableIntMaxDistanceNonBucketSet> {

    @Override
    MutableIntMaxDistanceNonBucketSet createSet(int initialCapacityExponent) {

        return new MutableIntMaxDistanceNonBucketSet(initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableIntMaxDistanceNonBucketSet set, P parameter, ForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach);
    }

    @Override
    boolean contains(MutableIntMaxDistanceNonBucketSet set, int value) {

        return set.contains(value);
    }

    @Override
    void add(MutableIntMaxDistanceNonBucketSet set, int value) {

        set.add(value);
    }

    @Override
    boolean addToSet(MutableIntMaxDistanceNonBucketSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableIntMaxDistanceNonBucketSet set, int value) {

        return set.remove(value);
    }
}
