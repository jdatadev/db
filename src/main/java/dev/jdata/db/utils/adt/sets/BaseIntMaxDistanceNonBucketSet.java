package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceIntSetOperations;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseIntMaxDistanceNonBucketSet extends BaseIntNonBucketSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_INT_MAX_DISTANCE_NON_BUCKET_MAP;

    private static final Class<?> debugClass = BaseIntMaxDistanceNonBucketSet.class;

    private byte[] maxDistances;

    BaseIntMaxDistanceNonBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor);
    }

    BaseIntMaxDistanceNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    @Override
    public final boolean contains(int value) {

        NonBucket.checkIsHashArrayElement(value);

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
    protected final int[] rehash(int[] hashArray, int newCapacity, int newKeyMask) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newKeyMask", newKeyMask));
        }

        this.maxDistances = new byte[newCapacity];

        final int[] result = super.rehash(hashArray, newCapacity, newKeyMask);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static final MaxDistanceIntSetOperations<BaseIntMaxDistanceNonBucketSet> maxDistanceOperations = new MaxDistanceIntSetOperations<BaseIntMaxDistanceNonBucketSet>() {

        @Override
        public long add(BaseIntMaxDistanceNonBucketSet set, int[] hashArray, int value, int hashArrayIndex) {

            return set.addValue(value);
        }

        @Override
        public void incrementNumElements(BaseIntMaxDistanceNonBucketSet hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseIntMaxDistanceNonBucketSet hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public byte[] getMaxDistances(BaseIntMaxDistanceNonBucketSet hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseIntMaxDistanceNonBucketSet hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public int[] getHashArray(BaseIntMaxDistanceNonBucketSet hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseIntMaxDistanceNonBucketSet hashed) {

            return hashed.getCapacity();
        }
    };

    final boolean addMaxDistance(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = MaxDistance.addValue(this, value, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }
}
