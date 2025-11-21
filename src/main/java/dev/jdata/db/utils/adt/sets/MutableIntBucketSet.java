package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.adt.hashed.helpers.IntBuckets;

public final class MutableIntBucketSet extends BaseIntBucketSet implements IMutableIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_BUCKET_SET;

    public static MutableIntBucketSet of(int ... values) {

        return new MutableIntBucketSet(values);
    }

    public MutableIntBucketSet(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    public MutableIntBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    private MutableIntBucketSet(int[] values) {
        super(values);
    }

    @Override
    public long getCapacity() {

        return getHashedCapacity();
    }

    @Override
    public void clear() {

        clearBaseIntBucketSet();
    }

    @Override
    public void addUnordered(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addValue(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public void addUnordered(IIntIterableElementsView elements) {

        Objects.requireNonNull(elements);

        if (DEBUG) {

            enter(b -> b.add("elements", elements));
        }

        if (elements instanceof MutableIntBucketSet) {

            final MutableIntBucketSet intSet = (MutableIntBucketSet)elements;

            final int noIntNode = IntBuckets.NO_INT_NODE;

            for (int value : intSet.getHashed()) {

                if (value != noIntNode) {

                    addUnordered(elements);
                }
            }
        }
        else {
            IBaseMutableIntSet.super.addUnordered(elements);
        }

        if (DEBUG) {

            exit(b -> b.add("elements", elements));
        }
    }

    @Override
    public boolean addToSet(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = addValue(value);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    @Override
    public boolean removeAtMostOne(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = removeElement(value);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public <T extends IBaseIntSet> T toImmutable(IBaseIntSetAllocator<T> intSetAllocator) {

        return intSetAllocator.copyToImmutable(this);
    }
}
