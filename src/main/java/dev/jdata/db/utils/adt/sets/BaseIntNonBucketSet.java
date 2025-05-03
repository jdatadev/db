package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntElements;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseIntNonBucketSet extends BaseIntegerSet<int[]> implements IIntElements {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_NON_BUCKET_SET;

//    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_INT_NON_BUCKET_MAP;

    private static final int NO_ELEMENT = -1;

    BaseIntNonBucketSet(int initialCapacityExponent, float loadFactor) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor);
    }

    BaseIntNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new, BaseIntNonBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ForEach<P, E> forEach) throws E {

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int element = hashArray[i];

            if (element != NO_ELEMENT) {

                forEach.each(element, parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    private int getIndexScanHashArrayToMax(int key, int max) {

        NonBucket.checkIsHashArrayElement(key);
        Checks.isLengthAboveZero(max);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("max", max));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int result = getIndexScanHashArrayToMax(key, hashArrayIndex, max);

        if (DEBUG) {

            exit(b -> b.add("key", key).add("max", max));
        }

        return result;
    }

    final int getIndexScanHashArrayToMax(int key, int hashArrayIndex, int max) {

        NonBucket.checkIsHashArrayElement(key);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int result = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), key, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return result;
    }

    final int getIndexScanEntireHashArray(int key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result = HashArray.getIndexScanEntireHashArray(getHashed(), key, getKeyMask());

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    final long addValue(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacity(1);

        final int[] hashArray = getHashed();

        final long putResult = add(hashArray, value);

        final boolean newAdded = IntPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(putResult, b -> b.add("key", value).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return putResult;
    }

    final int removeAndReturnIndex(int value) {

        NonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int result = HashArray.removeAndReturnIndex(getHashed(), value, getKeyMask(), this, i -> i.decrementNumElements());

        if (result == HashArray.NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected int[] rehash(int[] hashArray, int newCapacity, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("newKeyMask", newKeyMask));
        }

        final int[] newHashArray = new int[newCapacity];

        clearHashArray(newHashArray);

        final int mapLength = hashArray.length;

        for (int i = 0; i < mapLength; ++ i) {

            final int element = hashArray[i];

            if (element != NO_ELEMENT) {

                final long addResult = add(newHashArray, element);

                final int newIndex = IntPutResult.getPutIndex(addResult);

                newHashArray[newIndex] = element;
            }
        }

        if (DEBUG) {

            exit(newHashArray);
        }

        return newHashArray;
    }

    private long add(int[] hashArray, int value) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("value", value));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, value, getKeyMask());
        }

        final long result = add(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("value", value));
        }

        return result;
    }

    private long add(int[] hashArray, int value, int hashArrayIndex) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final long putResult = HashArray.add(hashArray, value, hashArrayIndex);

        final long result = putResult;
//        final boolean result = IntPutResult.getPutNewAdded(putResult);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    private int[] toArray() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final int[] result = new int[numElements];

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int element = hashArray[i];

            if (element != NO_ELEMENT) {

                result[dstIndex ++] = element;
            }
        }

        if (DEBUG) {

            exit();
        }

        return result;
    }

    final void clearBaseIntToIntNonBucketMap() {

        clearHashed();
    }

    private static void clearHashArray(int[] hashArray) {

        Arrays.fill(hashArray, NO_ELEMENT);
    }
}
