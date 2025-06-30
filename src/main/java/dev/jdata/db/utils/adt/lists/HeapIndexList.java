package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.HeapMutableIndexList.HeapMutableIndexListAllocator;

public final class HeapIndexList<T> extends IndexList<T> implements IHeapIndexList<T> {

    public static final class HeapIndexListAllocator<T> extends IndexListAllocator<T, HeapIndexList<T>, HeapIndexListBuilder<T>, HeapMutableIndexList<T>> {

        private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

        private final IntFunction<T[]> createElementsArray;
        private final HeapMutableIndexListAllocator<T> mutableIndexListAllocator;

        public HeapIndexListAllocator(IntFunction<T[]> createElementsArray) {
            this(createElementsArray, new HeapMutableIndexListAllocator<T>(createElementsArray));
        }

        private HeapIndexListAllocator(IntFunction<T[]> createElementsArray, HeapMutableIndexListAllocator<T> mutableIndexListAllocator) {

            this.createElementsArray = Objects.requireNonNull(createElementsArray);
            this.mutableIndexListAllocator = Objects.requireNonNull(mutableIndexListAllocator);
        }

        @Override
        HeapIndexListBuilder<T> allocateIndexListBuilder(int minimumCapacity) {

            return new HeapIndexListBuilder<>(ALLOCATION_TYPE, minimumCapacity, this);
        }

        @Override
        public void freeIndexListBuilder(HeapIndexListBuilder<T> builder) {

        }

        @Override
        HeapIndexList<T> allocateIndexListFrom(T[] values, int numElements) {

            return new HeapIndexList<>(ALLOCATION_TYPE, createElementsArray, values, numElements);
        }

        @Override
        public void freeIndexList(HeapIndexList<T> list) {

        }

        @Override
        HeapMutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

            return mutableIndexListAllocator.allocateMutableIndexList(minimumCapacity);
        }

        @Override
        void freeIndexMutableList(HeapMutableIndexList<T> list) {

        }

        @Override
        HeapIndexList<T> copyToImmutable(MutableIndexList<T> mutableIndexList) {

            Objects.requireNonNull(mutableIndexList);

            return fromMutableIndexList(ALLOCATION_TYPE, mutableIndexList);
        }

        @Override
        HeapIndexList<T> emptyList() {

            return HeapIndexList.empty();
        }
    }

    public static final class HeapIndexListBuilder<T> extends IndexListBuilder<T, HeapIndexList<T>, HeapIndexListBuilder<T>> {

        HeapIndexListBuilder(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
            this(allocationType, initialCapacity, new HeapIndexListAllocator<>(createElementsArray));
        }

        private HeapIndexListBuilder(AllocationType allocationType, int initialCapacity, HeapIndexListAllocator<T> listAllocator) {
            super(allocationType, initialCapacity, listAllocator);
        }

        @Override
        public HeapIndexList<T> build() {

            return fromMutableIndexList(AllocationType.HEAP_ALLOCATOR, getList());
        }
    }

    HeapIndexList(AllocationType allocationType) {
        super(allocationType);

        AllocationType.checkIsHeap(allocationType);
    }

    HeapIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    HeapIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    HeapIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    HeapIndexList(IndexList<T> toCopy) {
        super(toCopy);
    }

    @Override
    public HeapIndexList<T> toHeapAllocated() {

        return this;
    }
}
