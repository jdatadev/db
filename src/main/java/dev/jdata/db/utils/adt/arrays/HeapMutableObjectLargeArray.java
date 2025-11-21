package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

final class HeapMutableObjectLargeArray<T> extends MutableObjectLargeArray<T> implements IHeapMutableObjectLargeArray<T> {

    HeapMutableObjectLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray);
    }

    HeapMutableObjectLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray, T clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray, clearValue);
    }
}
