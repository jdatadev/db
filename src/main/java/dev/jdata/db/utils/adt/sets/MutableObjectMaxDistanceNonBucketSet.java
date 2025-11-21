package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.ObjectNonBucket;

abstract class MutableObjectMaxDistanceNonBucketSet<T> extends BaseObjectMaxDistanceNonBucketSet<T> implements IMutableSet<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_OBJECT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    MutableObjectMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<T[]> createHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed);
    }

    @Override
    public final long getCapacity() {

        return getHashedCapacity();
    }

    @Override
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseObjectSet();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean addToSet(T value) {

        ObjectNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = addMaxDistance(value);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    @Override
    public final boolean removeAtMostOne(T value) {

        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int index = removeMaxDistance(value);

        final boolean result = index != HashArray.NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }
}
