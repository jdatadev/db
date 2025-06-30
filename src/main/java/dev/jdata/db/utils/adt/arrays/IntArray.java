package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.IResettable;

public final class IntArray implements IIntArray, IIntArrayMutators, IResettable {

    private final LargeIntArray delegate;

    public IntArray(int initialOuterCapacity, int innerCapacityExponent) {

        this.delegate = new LargeIntArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    public boolean isEmpty() {

        return delegate.isEmpty();
    }

    @Override
    public long getCapacity() {

        return delegate.getCapacity();
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        delegate.toString(index, sb);
    }

    @Override
    public long getLimit() {

        return delegate.getLimit();
    }

    @Override
    public int get(int index) {

        return delegate.get(index);
    }

    @Override
    public void add(int value) {

        delegate.add(value);
    }

    @Override
    public void set(int index, int value) {

        delegate.set(index, value);
    }

    @Override
    public void reset() {

        delegate.clear();
    }
}
