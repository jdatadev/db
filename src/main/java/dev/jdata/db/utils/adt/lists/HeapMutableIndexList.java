package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

public final class HeapMutableIndexList<T> extends MutableIndexList<T> implements IHeapMutableIndexList<T> {

    static final class HeapMutableIndexListAllocator<T> extends MutableIndexListAllocator<T, HeapMutableIndexList<T>> {

        private final IntFunction<T[]> createElementsArray;

        public HeapMutableIndexListAllocator(IntFunction<T[]> createElementsArray) {

            this.createElementsArray = Objects.requireNonNull(createElementsArray);
        }

        @Override
        HeapMutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

            return new HeapMutableIndexList<>(AllocationType.HEAP_ALLOCATOR, createElementsArray);
        }

        @Override
        public void freeMutableIndexList(HeapMutableIndexList<T> list) {

        }
    }

    public static <T> HeapMutableIndexList<T> copyOf(IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {

        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(toCopy);

        return new HeapMutableIndexList<>(createElementsArray, toCopy);
    }

    public static <T> HeapMutableIndexList<T> from(IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(createElementsArray);

        return new HeapMutableIndexList<>(AllocationType.HEAP, createElementsArray);
    }

    static <T> HeapMutableIndexList<T> from(IntFunction<T[]> createElementsArray, int initialCapacity) {

        Objects.requireNonNull(createElementsArray);
        Checks.isInitialCapacity(initialCapacity);

        return new HeapMutableIndexList<>(AllocationType.HEAP, createElementsArray, initialCapacity);
    }

    static <T> HeapMutableIndexList<T> from(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(createElementsArray);
        Checks.isInitialCapacity(initialCapacity);

        return new HeapMutableIndexList<>(allocationType, createElementsArray, initialCapacity);
    }

    private HeapMutableIndexList(IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {
        super(createElementsArray, toCopy);
    }

    private HeapMutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    private HeapMutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    HeapMutableIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }
}
