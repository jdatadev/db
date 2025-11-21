package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.adt.hashed.helpers.IntBuckets;

abstract class MutableIntBucketSet extends BaseIntBucketSet implements IMutableIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_BUCKET_SET;

    MutableIntBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent));
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

        clearBaseBucketSet();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final void addUnordered(IIntIterableElementsView elements) {

        Objects.requireNonNull(elements);

        if (DEBUG) {

            enter(b -> b.add("elements", elements));
        }

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
