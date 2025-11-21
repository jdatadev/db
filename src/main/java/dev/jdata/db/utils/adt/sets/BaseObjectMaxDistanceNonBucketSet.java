package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceObjectSetOperations;
import dev.jdata.db.utils.adt.hashed.helpers.ObjectNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseObjectMaxDistanceNonBucketSet<T> extends BaseObjectNonBucketSet<T> implements IBaseObjectSetCommon<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_MAX_DISTANCE_NON_BUCKET_SET;

    private byte[] maxDistances;

    BaseObjectMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed);
    }

    BaseObjectMaxDistanceNonBucketSet(AllocationType allocationType, T[] values, IntFunction<T[]> createHashed) {
        super(allocationType, values, createHashed);
    }

    BaseObjectMaxDistanceNonBucketSet(AllocationType allocationType, BaseObjectMaxDistanceNonBucketSet<T> toCopy) {
        super(allocationType, toCopy);
    }

    @Override
    public final boolean contains(T value) {

        ObjectNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(value, getKeyMask());

        final int index = getIndexScanHashArrayToMax(value, hashArrayIndex, maxDistances[hashArrayIndex]);

        final boolean result = index != HashArray.NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    @Override
    public final void addUnordered(T value) {

        ObjectNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addMaxDistance(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public final void addUnordered(@SuppressWarnings("unchecked") T ... instances) {

        Checks.isNotEmpty(instances);

        if (DEBUG) {

            enter(b -> b.add("instances", instances));
        }

        for (T instance : instances) {

            addMaxDistance(instance);
        }

        if (DEBUG) {

            enter(b -> b.add("instances", instances));
        }
    }

    @Override
    public final void addUnordered(T[] instances, int startIndex, int numElements) {

        Checks.isNotEmpty(instances);
        Checks.checkIntAddFromArray(instances, startIndex, numElements);
        Checks.checkElements(instances, ObjectNonBucket::checkIsHashArrayElement);

        if (DEBUG) {

            enter(b -> b.add("instances", instances).add("startIndex", startIndex).add("numElements", numElements));
        }

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addMaxDistance(instances[i]);
        }

        if (DEBUG) {

            exit(b -> b.add("instances", instances).add("startIndex", startIndex).add("numElements", numElements));
        }
    }

    private final MaxDistanceObjectSetOperations<T, BaseObjectMaxDistanceNonBucketSet<T>> maxDistanceOperations
            = new MaxDistanceObjectSetOperations<T, BaseObjectMaxDistanceNonBucketSet<T>>() {

        @Override
        public long add(BaseObjectMaxDistanceNonBucketSet<T> set, T[] hashArray, T value, int hashArrayIndex) {

            return set.addValue(value);
        }

        @Override
        public void incrementNumElements(BaseObjectMaxDistanceNonBucketSet<T> hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseObjectMaxDistanceNonBucketSet<T> hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public byte[] getMaxDistances(BaseObjectMaxDistanceNonBucketSet<T> hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseObjectMaxDistanceNonBucketSet<T> hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public T[] getHashArray(BaseObjectMaxDistanceNonBucketSet<T> hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseObjectMaxDistanceNonBucketSet<T> hashed) {

            return hashed.getHashedCapacity();
        }
    };

    final boolean addMaxDistance(T value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = MaxDistance.addValue(this, value, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    final int removeMaxDistance(T element) {

        ObjectNonBucket.checkIsHashArrayElement(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final T[] hashArray = getHashed();

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(element, getKeyMask());

        final int indexToRemove = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(hashArray, element, hashArrayIndex, maxDistances[hashArrayIndex]);

        if (indexToRemove != HashArray.NO_INDEX) {

            @SuppressWarnings("unchecked")
            final T noElement = (T)NO_ELEMENT;

            hashArray[indexToRemove] = noElement;

            maxDistances[hashArrayIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(MaxDistance.computeDistance(indexToRemove, hashArrayIndex, hashArray.length));

            decrementNumElements();
        }

        if (DEBUG) {

            exit(indexToRemove);
        }

        return indexToRemove;
    }
}
