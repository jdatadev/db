package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntElements.ForEach;
import dev.jdata.db.utils.scalars.Integers;

public final class LargeLongBucketSetTest extends BaseImmutableIntegerSetTest<LargeLongBucketSet> {

    @Override
    LargeLongBucketSet createSet(int[] values) {

        return LargeLongBucketSet.of(1, 0, Array.toLongArray(values));
    }

    @Override
    <P> void forEach(LargeLongBucketSet set, P parameter, ForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, (e, p) -> forEach.each(Integers.checkLongToInt(e), p));
    }

    @Override
    boolean contains(LargeLongBucketSet set, int value) {

        return set.contains(value);
    }
}
