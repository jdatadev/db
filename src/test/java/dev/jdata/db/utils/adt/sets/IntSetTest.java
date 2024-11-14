package dev.jdata.db.utils.adt.sets;

public final class IntSetTest extends BaseSetTest<IntSet> {

    @Override
    IntSet createSet(int initialCapacityExponent) {

        return new IntSet(initialCapacityExponent);
    }

    @Override
    void add(IntSet set, int value) {

        set.add(value);
    }

    @Override
    boolean contains(IntSet set, int element) {

        return set.contains(element);
    }

    @Override
    boolean remove(IntSet set, int element) {

        return set.remove(element);
    }
}
