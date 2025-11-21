package dev.jdata.db.utils.adt.arrays;

import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;

abstract class BaseIntegerArray<T> extends BaseOneDimensionalArray<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INTEGER_ARRAY;

    BaseIntegerArray(AllocationType allocationType, T elementsArray, int limit, int capacity, boolean hasClearValue, IntFunction<T> createElementsArray) {
        super(allocationType, elementsArray, limit, capacity, hasClearValue, createElementsArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("elementsArray", elementsArray).add("limit", limit).add("capacity", capacity)
                    .add("hasClearValue", hasClearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntegerArray(AllocationType allocationType, BaseIntegerArray<T> toCopy, Function<T, T> copyElements) {
        super(allocationType, toCopy, copyElements);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyElements", copyElements));
        }

        if (DEBUG) {

            exit();
        }
    }
}
