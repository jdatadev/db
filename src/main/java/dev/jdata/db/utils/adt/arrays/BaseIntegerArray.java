package dev.jdata.db.utils.adt.arrays;

import java.util.function.Function;

import dev.jdata.db.DebugConstants;

abstract class BaseIntegerArray<T> extends BaseOneDimensionalArray<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INTEGER_ARRAY;

    BaseIntegerArray(T elements, int limit, int capacity, boolean hasClearValue) {
        super(elements, limit, capacity, hasClearValue);

        if (DEBUG) {

            enter(b -> b.add("elements", elements).add("limit", limit).add("capacity", capacity).add("hasClearValue", hasClearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntegerArray(BaseIntegerArray<T> toCopy, Function<T, T> copyElements) {
        super(toCopy, copyElements);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyElements", copyElements));
        }

        if (DEBUG) {

            exit();
        }
    }
}
