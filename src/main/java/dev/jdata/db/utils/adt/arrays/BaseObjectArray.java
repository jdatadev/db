package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;

abstract class BaseObjectArray<T> extends BaseOneDimensionalArray<T[]> implements IObjectArrayCommon<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_ARRAY;

    BaseObjectArray(AllocationType allocationType, T[] elementsArray, int limit, boolean hasClearValue, IntFunction<T[]> createElementsArray) {
        super(allocationType, elementsArray, limit, elementsArray.length, hasClearValue, createElementsArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("elementsArray", elementsArray).add("limit", limit).add("hasClearValue", hasClearValue)
                    .add("createElementsArray", createElementsArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectArray(AllocationType allocationType, BaseObjectArray<T> toCopy) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final T get(long index) {

        return getElementsArray()[IByIndexView.intIndex(index)];
    }

    @Override
    protected final T[] copyValues(T[] values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, values.length, startIndex, numElements);

        return Arrays.copyOfRange(values, intIndex(startIndex), intIndex(startIndex + numElements));
    }
}
