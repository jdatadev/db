package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntElements.ForEach;

public final class IntBucketSetTest extends BaseImmutableIntegerSetTest<IntBucketSet> {

    @Override
    IntBucketSet createSet(int[] values) {

        return IntBucketSet.of(values);
    }

    @Override
    <P> void forEach(IntBucketSet set, P parameter, ForEach<P, RuntimeException> forEach) {

        set.forEach(parameter, forEach);
    }

    @Override
    boolean contains(IntBucketSet set, int value) {

        return set.contains(value);
    }
}
