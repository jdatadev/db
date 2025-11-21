package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

public abstract class InheritableObjectInnerOuterNodeListValues<T> extends BaseObjectInnerOuterNodeListValues<T> {

    protected InheritableObjectInnerOuterNodeListValues(int initialOuterCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {
        super(initialOuterCapacity, createOuterArray, createArray);
    }
}
