package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.scalars.Integers;

public final class LongLargeBucketSetTest extends BaseImmutableIntegerSetTest<LongLargeBucketSet> {

    @Override
    LongLargeBucketSet createSet(int[] values) {

        return LongLargeBucketSet.of(1, 0, Array.toLongArray(values));
    }

    @Override
    <P> void forEach(LongLargeBucketSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, (e, p) -> forEach.each(Integers.checkLongToInt(e), p));
    }

    @Override
    boolean contains(LongLargeBucketSet set, int value) {

        return set.contains(value);
    }
}
