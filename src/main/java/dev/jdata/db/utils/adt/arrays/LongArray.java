package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.IClearable;

public final class LongArray implements ILongArray, IClearable, ILongArrayMutators {

    private static final int DEFAULT_INNER_CAPACITY_EXPONENT = 10;

    private final LargeLongArray delegate;

    public LongArray(int initialCapacity) {
        this(CapacityExponents.computeArrayOuterCapacity(initialCapacity, DEFAULT_INNER_CAPACITY_EXPONENT), DEFAULT_INNER_CAPACITY_EXPONENT);
    }

    public LongArray(int initialOuterCapacity, int innerCapacityExponent) {

        this.delegate = new LargeLongArray(initialOuterCapacity, innerCapacityExponent, null);
    }

    @Override
    public long getCapacity() {

        return delegate.getCapacity();
    }

    @Override
    public void clear() {

        delegate.clear();
    }

    @Override
    public long getLimit() {

        return delegate.getLimit();
    }

    @Override
    public boolean isEmpty() {

        return delegate.isEmpty();
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        delegate.toString(index, sb);
    }

    @Override
    public long get(long index) {

        return delegate.get(index);
    }

    @Override
    public void add(long value) {

        delegate.add(value);
    }

    @Override
    public void set(int index, long value) {

        delegate.set(index, value);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [delegate=" + delegate + "]";
    }
}
