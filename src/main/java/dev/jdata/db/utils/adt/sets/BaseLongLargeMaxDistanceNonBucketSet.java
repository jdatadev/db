package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IHeapMutableByteLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableByteLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LargeMaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.LargeMaxDistance.LargeMaxDistanceLongSetOperations;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongLargeMaxDistanceNonBucketSet extends BaseLongLargeNonBucketSet implements IBaseLongSetCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_LARGE_MAX_DISTANCE_NON_BUCKET_SET;

    private IMutableByteLargeArray maxDistances;

    BaseLongLargeMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean contains(long value) {

        LongNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, getKeyMask());

        final long index = getIndexScanHashArrayToMax(value, hashArrayIndex, maxDistances.get(hashArrayIndex));

        final boolean result = index != LargeHashArray.NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    @Override
    protected final IMutableLongLargeArray rehash(IMutableLongLargeArray hashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        this.maxDistances = IHeapMutableByteLargeArray.create(newCapacity);

        final IMutableLongLargeArray result = super.rehash(hashArray, newCapacity, newCapacityExponent, newKeyMask);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        return result;
    }

    private static final LargeMaxDistanceLongSetOperations<BaseLongLargeMaxDistanceNonBucketSet> maxDistanceOperations
            = new LargeMaxDistanceLongSetOperations<BaseLongLargeMaxDistanceNonBucketSet>() {

        @Override
        public long add(BaseLongLargeMaxDistanceNonBucketSet set, IMutableLongLargeArray hashArray, long value, long hashArrayIndex) {

            return set.addValue(value);
        }

        @Override
        public void incrementNumElements(BaseLongLargeMaxDistanceNonBucketSet hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseLongLargeMaxDistanceNonBucketSet hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public IMutableByteLargeArray getMaxDistances(BaseLongLargeMaxDistanceNonBucketSet hashed) {

            return hashed.maxDistances;
        }

        @Override
        public long getKeyMask(BaseLongLargeMaxDistanceNonBucketSet hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public IMutableLongLargeArray getHashArray(BaseLongLargeMaxDistanceNonBucketSet hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseLongLargeMaxDistanceNonBucketSet hashed) {

            return hashed.getHashedCapacity();
        }
    };

    final boolean addMaxDistance(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = LargeMaxDistance.addValue(this, value, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    final long removeMaxDistance(long element) {

        LongNonBucket.checkIsHashArrayElement(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final IMutableLongLargeArray hashArray = getHashed();

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(element, getKeyMask());

        final long indexToRemove = LargeHashArray.getIndexScanHashArrayToMaxHashArrayIndex(hashArray, element, hashArrayIndex, maxDistances.get(hashArrayIndex));

        if (indexToRemove != HashArray.NO_INDEX) {

            hashArray.set(indexToRemove, NO_ELEMENT);

            final long distance = LargeMaxDistance.computeDistance(indexToRemove, hashArrayIndex, hashArray.getLimit());

            maxDistances.set(hashArrayIndex, Integers.checkUnsignedLongToUnsignedByteAsByte(distance));

            decrementNumElements();
        }

        if (DEBUG) {

            exit(indexToRemove);
        }

        return indexToRemove;
    }
}
