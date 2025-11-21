package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;

final class HeapMutableObjectLargeArray<T> extends MutableObjectLargeArray<T> implements IHeapMutableObjectLargeArray<T> {

    static <T> HeapMutableObjectLargeArray<T> create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableObjectLargeArray<>(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray);
    }

    static <T> HeapMutableObjectLargeArray<T> create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray, T clearValue) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableObjectLargeArray<>(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray, clearValue);
    }

    private HeapMutableObjectLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray);

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        Objects.requireNonNull(createOuterArray);
        Objects.requireNonNull(createInnerArray);
    }

    private HeapMutableObjectLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, IntFunction<T[][]> createOuterArray,
            IntFunction<T[]> createInnerArray, T clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, createOuterArray, createInnerArray, clearValue);

        AllocationType.checkIsHeap(allocationType);
    }
}
