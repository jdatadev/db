package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongLargeBucketSetTest extends BaseMutableIntegerSetTest<MutableLongLargeBucketSet> {

    @Override
    MutableLongLargeBucketSet createSet(int initialCapacityExponent) {

        return new MutableLongLargeBucketSet(1, initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableLongLargeBucketSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach != null ? (e, p) -> forEach.each(Integers.checkUnsignedLongToUnsignedInt(e), p) : null);
    }

    @Override
    void add(MutableLongLargeBucketSet set, int value) {

        set.add(value);
    }

    @Override
    boolean addToSet(MutableLongLargeBucketSet set, int value) {

        return set.addToSet(value);
    }

    @Override
    boolean remove(MutableLongLargeBucketSet set, int value) {

        return set.removeAtMostOne(value);
    }

    @Override
    boolean contains(MutableLongLargeBucketSet set, int value) {

        return set.contains(value);
    }
}
