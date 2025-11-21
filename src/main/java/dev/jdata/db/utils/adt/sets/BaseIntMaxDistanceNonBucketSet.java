package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashUtil;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceIntSetOperations;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntMaxDistanceNonBucketSet extends BaseIntNonBucketSet implements IBaseIntSetCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_MAX_DISTANCE_NON_BUCKET_SET;

    private byte[] maxDistances;

    BaseIntMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        constructorInitialize();
    }

    BaseIntMaxDistanceNonBucketSet(AllocationType allocationType, int[] values) {
        super(allocationType, values);

        constructorInitialize();

        addUnordered(values);
    }

    BaseIntMaxDistanceNonBucketSet(AllocationType allocationType, int capacityExponentIncrease, float loadFactor, IIntIterableElementsView mutableFrom) {
        super(allocationType, HashUtil.computeHashCapacityExponent(mutableFrom.getNumIterableElements(), loadFactor), capacityExponentIncrease, loadFactor);

        constructorInitialize();

        addUnordered(mutableFrom);
    }

    BaseIntMaxDistanceNonBucketSet(AllocationType allocationType, BaseIntMaxDistanceNonBucketSet toCopy) {
        super(allocationType, toCopy);

        constructorInitialize();

        addUnordered(toCopy);
    }

    private void constructorInitialize() {

        initialize(getHashedCapacity());
    }

    @Override
    public final boolean contains(int value) {

        IntNonBucket.checkIsHashArrayElement(value);

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
    public final void addUnordered(int value) {

        IntNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addMaxDistance(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public final void addUnordered(int... values) {

        Checks.isNotEmpty(values);

        if (DEBUG) {

            enter(b -> b.add("values", values));
        }

        for (int value : values) {

            addMaxDistance(value);
        }

        if (DEBUG) {

            exit(b -> b.add("values", values));
        }
    }

    @Override
    public final void addUnordered(int[] values, int startIndex, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkIntAddFromArray(values, startIndex, numElements);
        Checks.checkElements(values, IntNonBucket::checkIsHashArrayElement);

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

    @Override
    protected final void rehashWithCapacity(int[] hashed, int[] newHashed, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed).add("newHashed", newHashed).add("newCapacity", newCapacity));
        }

        initialize(newCapacity);

        super.rehashWithCapacity(hashed, newHashed, newCapacity);

        if (DEBUG) {

            exit(b -> b.add("hashed", hashed).add("newHashed", newHashed).add("newCapacity", newCapacity));
        }
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

            return hashed.getHashedCapacity();
        }
    };

    final boolean addMaxDistance(int value) {

        IntNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = MaxDistance.addValue(this, value, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    final int removeMaxDistance(int element) {

        IntNonBucket.checkIsHashArrayElement(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int[] hashArray = getHashed();

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
