package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;

abstract class BaseObjectArray<T> extends BaseOneDimensionalArray<T[]> implements IObjectArrayCommon<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_ARRAY;

    BaseObjectArray(T[] elements, int limit, boolean hasClearValue) {
        super(elements, limit, elements.length, hasClearValue);

        if (DEBUG) {

            enter(b -> b.add("elements", elements).add("limit", limit).add("hasClearValue", hasClearValue));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectArray(BaseObjectArray<T> toCopy) {
        super(toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final T get(long index) {

        return elements[IByIndexView.intIndex(index)];
    }
}
