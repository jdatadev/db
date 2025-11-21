package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashUtil;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceLongSetOperations;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongMaxDistanceNonBucketSet extends BaseLongNonBucketSet implements IBaseLongSetCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_MAX_DISTANCE_NON_BUCKET_SET;

    private byte[] maxDistances;

    BaseLongMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        constructorInitialize();
    }

    BaseLongMaxDistanceNonBucketSet(AllocationType allocationType, int capacityExponentIncrease, float loadFactor, ILongIterableElementsView mutableFrom) {
        super(allocationType, HashUtil.computeHashCapacityExponent(mutableFrom.getNumIterableElements(), loadFactor), capacityExponentIncrease, loadFactor);

        constructorInitialize();

        addUnordered(mutableFrom);
    }

    BaseLongMaxDistanceNonBucketSet(AllocationType allocationType, BaseLongMaxDistanceNonBucketSet toCopy) {
        super(allocationType, toCopy);

        constructorInitialize();

        addUnordered(toCopy);
    }

    private void constructorInitialize() {

        initialize(getHashedCapacity());
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
    public final void addUnordered(long value) {

        LongNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addMaxDistance(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public final void addUnordered(long... values) {

        Checks.isNotEmpty(values);

        if (DEBUG) {

            enter(b -> b.add("values", values));
        }

        for (long value : values) {

            addMaxDistance(value);
        }

        if (DEBUG) {

            exit(b -> b.add("values", values));
        }
    }

    @Override
    public final void addUnordered(long[] values, int startIndex, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkIntAddFromArray(values, startIndex, numElements);
        Checks.checkElements(values, LongNonBucket::checkIsHashArrayElement);

        if (DEBUG) {

            enter(b -> b.add("values", values).add("startIndex", startIndex).add("numElements", numElements));
        }

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addMaxDistance(values[i]);
        }

        if (DEBUG) {

            exit(b -> b.add("values", values).add("startIndex", startIndex).add("numElements", numElements));
        }
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

    private void initialize(int capacity) {

        this.maxDistances = new byte[capacity];
    }
}
