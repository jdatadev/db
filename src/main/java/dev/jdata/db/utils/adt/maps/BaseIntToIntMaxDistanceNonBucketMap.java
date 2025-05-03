package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceIntMapOperations;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntToIntMaxDistanceNonBucketMap extends BaseIntToIntNonBucketMap implements IIntContainsKeyMap, IIntToIntBucketMapGetters {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_INT_MAX_DISTANCE_NON_BUCKET_MAP;

    private static final Class<?> debugClass = BaseIntToIntMaxDistanceNonBucketMap.class;

    private byte[] maxDistances;

    BaseIntToIntMaxDistanceNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);

        initialize();
    }

    BaseIntToIntMaxDistanceNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);

        initialize();
    }

    BaseIntToIntMaxDistanceNonBucketMap(BaseIntToIntNonRemoveNonBucketMap toCopy) {
        super(toCopy);

        initialize();
    }

    private void initialize() {

        this.maxDistances = new byte[getCapacity()];
    }

    @Override
    public final boolean containsKey(int key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final boolean result = getIndexScanHashArrayToMax(key, hashArrayIndex, maxDistances[hashArrayIndex]) != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final int get(int key, int defaultValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int index = getIndexScanHashArrayToMax(key, hashArrayIndex, maxDistances[hashArrayIndex]);

        final int result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
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

    private static final MaxDistanceIntMapOperations<BaseIntToIntMaxDistanceNonBucketMap, int[]> maxDistanceOperations
            = new MaxDistanceIntMapOperations<BaseIntToIntMaxDistanceNonBucketMap, int[]>() {

        @Override
        public long put(BaseIntToIntMaxDistanceNonBucketMap hashed, int[] hashArray, int key, int hashArrayIndex) {

            return hashed.put(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public int[] getValues(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public int[] getHashArray(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getCapacity();
        }
    };

    final int putMaxDistance(int key, int value, int defaultPreviousValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        checkCapacity(1);

        final int result = MaxDistance.putMaxDistance(this, key, value, defaultPreviousValue, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    final int removeMaxDistance(int key, int defaultValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int index = removeAndReturnIndex(key, hashArrayIndex);

        if (index != NO_INDEX) {

            final int distance = MaxDistance.computeDistance(index, hashArrayIndex, getCapacity());

            maxDistances[index] = Integers.checkUnsignedIntToUnsignedByte(distance);
        }

        final int result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }
}
