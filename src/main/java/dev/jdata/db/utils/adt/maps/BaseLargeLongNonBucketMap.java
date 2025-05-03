package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

@Deprecated // currently not in use
abstract class BaseLargeLongNonBucketMap<T> extends BaseLargeIntegerMap<LargeLongArray, T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_LONG_NON_BUCKET_MAP;

    protected static final long NO_KEY = -1L;

    BaseLargeLongNonBucketMap(int initialOuterCapacity, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createValues) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, (o, i) -> new LargeLongArray(o, i, NO_KEY), LargeLongArray::reset, createValues);
    }

    @Override
    protected final LargeLongArray rehash(LargeLongArray hashed, long newCapacity, long keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed).add("newCapacity", newCapacity).add("keyMask", keyMask));
        }

        if (Boolean.TRUE) {

            throw new UnsupportedOperationException();
        }

        return null;
/*
        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacity = computeOuterCapacity(newCapacity, innerCapacityExponent);

        final LargeLongArray newKeys = new LargeLongArray(newOuterCapacity, innerCapacityExponent, NO_KEY);
        final LargeLongMultiHeadSinglyLinkedList<LargeLongSet> newBuckets = createBuckets(newOuterCapacity, innerCapacityExponent);

        clearKeys(newKeys);

        final long setLength = hashed.getCapacity();

        for (long i = 0; i < setLength; ++ i) {

            final long element = hashed.get(i);

            if (element != NO_KEY) {

                add(newKeys, newBuckets, element);
            }
        }

        this.buckets = newBuckets;

        if (DEBUG) {

            exit(newKeys);
        }

        return newKeys;
*/
    }

    private static void clearKeys(LargeLongArray keys) {

    }
}
