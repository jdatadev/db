package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableSet<T> extends IMutableSet<T> {

    public static <T> IHeapMutableSet<T> create(int initialCapacity, IntFunction<T[]> createHashed) {

        return HeapMutableObjectMaxDistanceNonBucketSet.create(AllocationType.HEAP, initialCapacity, createHashed);
    }
}
