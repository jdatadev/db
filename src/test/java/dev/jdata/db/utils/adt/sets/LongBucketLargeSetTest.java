package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class LongBucketLargeSetTest extends BaseImmutableIntegerSetTest<LongBucketLargeSet> {

    @Override
    LongBucketLargeSet createSet(int[] values) {

        return HeapLongBucketLargeSet.of(AllocationType.HEAP, 1, 1, Array.toLongArray(values));
    }

    @Override
    <P> void forEach(LongBucketLargeSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, (e, p) -> forEach.each(Integers.checkLongToInt(e), p));
    }

    @Override
    boolean contains(LongBucketLargeSet set, int value) {

        return set.contains(value);
    }
}
