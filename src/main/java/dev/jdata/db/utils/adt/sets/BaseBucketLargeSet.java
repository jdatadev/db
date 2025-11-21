package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.arrays.IMutableOneDimensionalArray;
import dev.jdata.db.utils.adt.lists.IMultiHeadNodeListMutable;
import dev.jdata.db.utils.adt.lists.ISinglyLinkedMultiHeadNodeListView;
import dev.jdata.db.utils.adt.lists.LargeNodeLists;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseBucketLargeSet<T extends IMutableLargeArrayMarker & IMutableOneDimensionalArray, U extends ISinglyLinkedMultiHeadNodeListView & IMultiHeadNodeListMutable<?>>

        extends BaseLongCapacityExponentSet<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_BUCKET_SET;

    static final long NO_LONG_NODE = LargeNodeLists.NO_LONG_NODE;

    private U buckets;

    abstract void rehashBuckets(T hashArray, T newHashArray, long newKeyMask, U buckets, U newBuckets);

    BaseBucketLargeSet(AllocationType allocationType, int initialOuterCapacity, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(allocationType, initialOuterCapacity, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    @Override
    protected final void rehashWithKeyMask(T hashArray, T newHashArray, long newCapacity, int capacityExponentIncrease, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final U oldBuckets = buckets;

        @SuppressWarnings("unchecked")
        final U newBuckets = this.buckets = (U)oldBuckets.createEmptyWithCapacityExponentIncrease(capacityExponentIncrease);

        rehashBuckets(hashArray, newHashArray, newKeyMask, oldBuckets, newBuckets);

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).add("newCapacity", newCapacity).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }
    }

    final U getBuckets() {
        return buckets;
    }

    final void clearBaseBucketLargeSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        buckets.clear();

        if (DEBUG) {

            exit();
        }
    }
}
