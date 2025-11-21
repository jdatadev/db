package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntForEach;

public final class IntBucketSetTest extends BaseImmutableIntegerSetTest<IntBucketSet> {

    @Override
    IntBucketSet createSet(int[] values) {

        return IntBucketSet.of(values);
    }

    @Override
    <P> void forEach(IntBucketSet set, P parameter, IIntForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach);
    }

    @Override
    boolean contains(IntBucketSet set, int value) {

        return set.contains(value);
    }
}
