package dev.jdata.db.utils.adt.sets;

public final class LongSetTest extends BaseSetTest<LongSet> {

    @Override
    LongSet createSet(int initialCapacityExponent) {

        return new LongSet(initialCapacityExponent);
    }

    @Override
    void add(LongSet set, int value) {

        set.add(value);
    }

    @Override
    boolean contains(LongSet set, int element) {

        return set.contains(element);
    }

    @Override
    boolean remove(LongSet set, int element) {

        return set.remove(element);
    }
}
