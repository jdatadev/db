package dev.jdata.db.utils.adt.arrays;

public final class LongArray implements LongArrayGetters {

    private final LongLargeArray delegate;

    public LongArray(int initialOuterCapacity, int innerCapacityExponent) {

        this.delegate = new LongLargeArray(initialOuterCapacity, innerCapacityExponent);
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
    public long get(int index) {

        return delegate.get(index);
    }

    public void set(int index, long value) {

        delegate.set(index, value);
    }

    public void add(long value) {

        delegate.add(value);
    }

    public void clear() {

        delegate.clear();
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [delegate=" + delegate + "]";
    }
}
