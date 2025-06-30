package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntIterableElements.IForEach;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLargeLongBucketSetTest extends BaseMutableIntegerSetTest<MutableLargeLongBucketSet> {

    @Override
    MutableLargeLongBucketSet createSet(int initialCapacityExponent) {

        return new MutableLargeLongBucketSet(1, initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableLargeLongBucketSet set, P parameter, IForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach != null ? (e, p) -> forEach.each(Integers.checkUnsignedLongToUnsignedInt(e), p) : null);
    }

    @Override
    void add(MutableLargeLongBucketSet set, int value) {

        set.add(value);
    }

    @Override
    boolean addToSet(MutableLargeLongBucketSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableLargeLongBucketSet set, int value) {

        return set.removeAtMostOne(value);
    }

    @Override
    boolean contains(MutableLargeLongBucketSet set, int value) {

        return set.contains(value);
    }
}
