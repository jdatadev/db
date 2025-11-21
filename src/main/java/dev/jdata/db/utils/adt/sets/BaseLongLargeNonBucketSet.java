package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.ILongArrayView;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongLargeNonBucketSet extends BaseIntegerLargeNonBucketSet<IMutableLongLargeArray> implements IBaseLongSetCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_LARGE_NON_BUCKET_SET;

    static final long NO_ELEMENT = LongNonBucket.NO_ELEMENT;

    BaseLongLargeNonBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_INNER_CAPACITY_EXPONENT, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongLargeNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, BaseLongLargeNonBucketSet::createHashArray,
                BaseLongLargeNonBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongLargeNonBucketSet(AllocationType allocationType, BaseLongLargeNonBucketSet toCopy) {
        super(allocationType, toCopy, IHeapMutableLongLargeArray::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final ILongArrayView hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long element = hashArray.get(i);

            if (element != NO_ELEMENT) {

                forEach.each(element, parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        R result = defaultResult;

        final ILongArrayView hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long element = hashArray.get(i);

            if (element != NO_ELEMENT) {

                final R forEachResult = forEach.each(element, parameter1, parameter2);

                if (forEachResult != null) {

                    result = forEachResult;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        return result;
    }

    final long getIndexScanHashArrayToMax(long value, long hashArrayIndex, int max) {

        LongNonBucket.checkIsHashArrayElement(value);
        Checks.isIntLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("value", value).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final long result = LargeHashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), value, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("value", value).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return result;
    }

    final long addValue(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacity(1);

        final IMutableLongLargeArray hashArray = getHashed();

        final long putResult = add(hashArray, value);

        final boolean newAdded = LongCapacityPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exitWithHex(putResult, b -> b.add("key", value).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return putResult;
    }

    @Override
    public final IHeapLongSet toHeapAllocated() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final long[] toArray() {

        if (DEBUG) {

            enter();
        }

        final int numElements = IOnlyElementsView.intNumElements(this);

        final long[] result = new long[numElements];

        final ILongArrayView hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final long noElement = NO_ELEMENT;

        int dstIndex = 0;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long element = hashArray.get(i);

            if (element != noElement) {

                result[dstIndex ++] = element;
            }
        }

        if (DEBUG) {

            exit();
        }

        return result;
    }

    @Override
    protected IMutableLongLargeArray rehash(IMutableLongLargeArray hashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final IMutableLongLargeArray newHashArray = IHeapMutableLongLargeArray.create(newCapacity);

        clearHashArray(newHashArray);

        final long hashArrayLimit = hashArray.getLimit();

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long element = hashArray.get(i);

            if (element != NO_ELEMENT) {

                final long addResult = add(newHashArray, element);

                final long newIndex = LongCapacityPutResult.getPutIndex(addResult);

                newHashArray.set(newIndex, element);
            }
        }

        if (DEBUG) {

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        return newHashArray;
    }

    private long add(IMutableLongLargeArray hashArray, long value) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("value", value));
        }

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, value, getKeyMask());
        }

        final long result = add(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("value", value));
        }

        return result;
    }

    private long add(IMutableLongLargeArray hashArray, long value, long hashArrayIndex) {

        LongNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = LargeHashArray.add(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final void clearBaseIntToIntNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    private static IMutableLongLargeArray createHashArray(int initialOuterCapacity, int innerCapacityExponent) {

        return IHeapMutableLongLargeArray.create(initialOuterCapacity, innerCapacityExponent, NO_ELEMENT);
    }

    private static void clearHashArray(IMutableLongLargeArray hashArray) {

        LongNonBucket.clearHashArray(hashArray);
    }
}
