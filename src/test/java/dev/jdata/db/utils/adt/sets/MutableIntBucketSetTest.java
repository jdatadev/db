package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntElements.ForEach;

public final class MutableIntBucketSetTest extends BaseMutableIntegerSetTest<MutableIntBucketSet> {

    @Override
    MutableIntBucketSet createSet(int initialCapacityExponent) {

        return new MutableIntBucketSet(initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableIntBucketSet set, P parameter, ForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach);
    }

    @Override
    boolean contains(MutableIntBucketSet set, int value) {

        return set.contains(value);
    }

    @Override
    void add(MutableIntBucketSet set, int value) {

        set.add(value);
    }

    @Override
    boolean addToSet(MutableIntBucketSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableIntBucketSet set, int value) {

        return set.remove(value);
    }
}
