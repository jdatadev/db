package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.hashed.BaseLongCapacityExponentArrayHashed;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseNonBucketLargeSet<T extends IMutableLargeArrayMarker> extends BaseArrayLargeSet<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_NON_BUCKET_LARGE_SET;

    BaseNonBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }

    BaseNonBucketLargeSet(AllocationType allocationType, BaseLongCapacityExponentArrayHashed<T> toCopy, Function<T, T> copyHashed) {
        super(allocationType, toCopy, copyHashed);
    }

    final void clearNonBucketLargeSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }
}
