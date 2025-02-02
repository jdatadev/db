package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.Clearable;

public final class IntArray implements IIntArray, Clearable {

    private final IntLargeArray delegate;

    public IntArray(int initialOuterCapacity, int innerCapacityExponent) {

        this.delegate = new IntLargeArray(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    public boolean isEmpty() {

        return delegate.isEmpty();
    }

    @Override
    public long getNumElements() {

        return delegate.getNumElements();
    }

    @Override
    public int get(int index) {

        return delegate.get(index);
    }

    public void set(int index, int value) {

        delegate.set(index, value);
    }

    public void add(int value) {

        delegate.add(value);
    }

    @Override
    public final void clear() {

        delegate.clear();
    }
}
