package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceLongSetOperations;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongMaxDistanceNonBucketSet extends BaseLongNonBucketSet implements IBaseLongSetCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_MAX_DISTANCE_NON_BUCKET_SET;

    private byte[] maxDistances;

    BaseLongMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongMaxDistanceNonBucketSet(AllocationType allocationType, BaseLongMaxDistanceNonBucketSet toCopy) {
        super(allocationType, toCopy);
    }

    @Override
    public final boolean contains(long value) {

        LongNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, getKeyMask());

        final int index = getIndexScanHashArrayToMax(value, hashArrayIndex, maxDistances[hashArrayIndex]);

        final boolean result = index != HashArray.NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    @Override
    protected final long[] rehash(long[] hashArray, int newCapacity, int newCapacityExponent, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        this.maxDistances = new byte[newCapacity];

        final long[] result = super.rehash(hashArray, newCapacity, newCapacityExponent, newKeyMask);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        return result;
    }

    private static final MaxDistanceLongSetOperations<BaseLongMaxDistanceNonBucketSet> maxDistanceOperations = new MaxDistanceLongSetOperations<BaseLongMaxDistanceNonBucketSet>() {

        @Override
        public long add(BaseLongMaxDistanceNonBucketSet set, long[] hashArray, long value, int hashArrayIndex) {

            return set.addValue(value);
        }

        @Override
        public void incrementNumElements(BaseLongMaxDistanceNonBucketSet hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseLongMaxDistanceNonBucketSet hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public byte[] getMaxDistances(BaseLongMaxDistanceNonBucketSet hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseLongMaxDistanceNonBucketSet hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public long[] getHashArray(BaseLongMaxDistanceNonBucketSet hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseLongMaxDistanceNonBucketSet hashed) {

            return hashed.getHashedCapacity();
        }
    };

    final boolean addMaxDistance(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = MaxDistance.addValue(this, value, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    final int removeMaxDistance(long element) {

        LongNonBucket.checkIsHashArrayElement(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final long[] hashArray = getHashed();

        final int hashArrayIndex = HashFunctions.hashArrayIndex(element, getKeyMask());

        final int indexToRemove = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(hashArray, element, hashArrayIndex, maxDistances[hashArrayIndex]);

        if (indexToRemove != HashArray.NO_INDEX) {

            hashArray[indexToRemove] = NO_ELEMENT;

            maxDistances[hashArrayIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(MaxDistance.computeDistance(indexToRemove, hashArrayIndex, hashArray.length));

            decrementNumElements();
        }

        if (DEBUG) {

            exit(indexToRemove);
        }

        return indexToRemove;
    }
}
