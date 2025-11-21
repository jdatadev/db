package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntIterableOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.helpers.IntBuckets;

abstract class MutableIntBucketSet extends BaseIntBucketSet implements IMutableIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_BUCKET_SET;

    MutableIntBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        this(allocationType, initialCapacityExponent, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableIntBucketSet(AllocationType allocationType, int[] values) {
        super(allocationType, values);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("values", values));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long getCapacity() {

        return getHashedCapacity();
    }

    @Override
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseIntBucketSet();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final void addUnordered(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addValue(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public final void addUnordered(IIntIterableOnlyElementsView elements) {

        if (DEBUG) {

            enter(b -> b.add("elements", elements));
        }

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
            IMutableIntSet.super.addUnordered(elements);
        }

        if (DEBUG) {

            exit(b -> b.add("elements", elements));
        }
    }

    @Override
    public final boolean addToSet(int value) {

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
    public final boolean removeAtMostOne(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = removeElement(value);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
