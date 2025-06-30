package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntIterableElements.IForEach;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongBucketSetTest extends BaseMutableIntegerSetTest<MutableLongBucketSet> {

    @Override
    MutableLongBucketSet createSet(int initialCapacityExponent) {

        return new MutableLongBucketSet(initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableLongBucketSet set, P parameter, IForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach != null ? (e, p) -> forEach.each(Integers.checkUnsignedLongToUnsignedInt(e), p) : null);
    }

    @Override
    boolean contains(MutableLongBucketSet set, int value) {

        return set.contains(value);
    }

    @Override
    void add(MutableLongBucketSet set, int value) {

        set.add(value);
    }

    @Override
    boolean addToSet(MutableLongBucketSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableLongBucketSet set, int value) {

        return set.removeAtMostOne(value);
    }
}
