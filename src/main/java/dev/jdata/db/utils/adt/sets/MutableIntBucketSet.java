package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntElements;
import dev.jdata.db.utils.adt.hashed.helpers.Buckets;

public final class MutableIntBucketSet extends BaseIntBucketSet implements IMutableIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_BUCKET_SET;

    public static MutableIntBucketSet of(int ... values) {

        return new MutableIntBucketSet(values);
    }

    public MutableIntBucketSet(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableIntBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor);
    }

    private MutableIntBucketSet(int[] values) {
        super(values);
    }

    @Override
    public void clear() {

        clearBaseIntBucketSet();
    }

    @Override
    public void add(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addValue(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public void addAll(IIntElements intElements) {

        Objects.requireNonNull(intElements);

        if (intElements instanceof MutableIntBucketSet) {

            final MutableIntBucketSet intSet = (MutableIntBucketSet)intElements;

            final int noIntNode = Buckets.NO_INT_NODE;

            for (int value : intSet.getHashed()) {

                if (value != noIntNode) {

                    add(value);
                }
            }
        }
        else {
            IMutableIntSet.super.addAll(intElements);
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
    public boolean remove(int value) {

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
