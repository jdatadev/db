package dev.jdata.db.utils.adt.sets;

import java.util.function.Consumer;

import dev.jdata.db.utils.adt.arrays.IMutableLargeArrayMarker;
import dev.jdata.db.utils.adt.lists.LargeNodeLists;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeIntegerBucketSet<T extends IMutableLargeArrayMarker> extends BaseLargeIntegerSet<T> {

    static final long NO_LONG_NODE = LargeNodeLists.NO_LONG_NODE;

    BaseLargeIntegerBucketSet(AllocationType allocationType, int initialOuterCapacity, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<T> createHashed, Consumer<T> clearHashed) {
        super(allocationType, initialOuterCapacity, capacityExponentIncrease, innerCapacityExponent, loadFactor, createHashed, clearHashed);
    }
}
