package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongBucketSetTest extends BaseMutableIntegerSetTest<MutableLongBucketSet> {

    @Override
    MutableLongBucketSet createSet(int initialCapacityExponent) {

        return new HeapMutableLongBucketSet(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    <P> void forEach(MutableLongBucketSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach != null ? (e, p) -> forEach.each(Integers.checkUnsignedLongToUnsignedInt(e), p) : null);
    }

    @Override
    boolean contains(MutableLongBucketSet set, int value) {

        return set.contains(value);
    }

    @Override
    void addInAnyOrder(MutableLongBucketSet set, int value) {

        set.addInAnyOrder(value);
    }

    @Override
    void addUnordered(MutableLongBucketSet set, int value) {

        set.addUnordered(value);
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
