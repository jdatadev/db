package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

final class HeapObjectIndexListBuilder<T>

        extends ObjectIndexListBuilder<T, IHeapIndexList<T>, IHeapIndexList<T>, MutableObjectIndexList<T>, IHeapIndexListBuilder<T>>
        implements IHeapIndexListBuilder<T> {

    static <T> HeapObjectIndexListBuilder<T> create(AllocationType allocationType, IntFunction<T[]> createElementsArray) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP);

        return new HeapObjectIndexListBuilder<>(allocationType, createElementsArray);
    }

    static <T> HeapObjectIndexListBuilder<T> create(AllocationType allocationType, int initialCapacity, IntFunction<T[]> createElementsArray) {

        checkBuilderCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);
        Objects.requireNonNull(createElementsArray);

        return new HeapObjectIndexListBuilder<>(allocationType, initialCapacity, createElementsArray);
    }

    private HeapObjectIndexListBuilder(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY, createElementsArray);
    }

    private HeapObjectIndexListBuilder(AllocationType allocationType, int initialCapacity, IntFunction<T[]> createElementsArray) {
        this(allocationType, initialCapacity, new HeapMutableObjectIndexListAllocator<>(createElementsArray), (t, c, a) -> a.allocateMutable(createElementsArray, c));
    }

    private <P> HeapObjectIndexListBuilder(AllocationType allocationType, int initialCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MutableObjectIndexList<T>, P> builderMutableAllocator) {
        super(allocationType, initialCapacity, parameter, builderMutableAllocator);
    }

    @Override
    protected IHeapIndexList<T> empty() {

        return HeapObjectIndexList.empty();
    }

    @Override
    protected IHeapIndexList<T> heapEmpty() {

        return empty();
    }

    @Override
    protected IHeapIndexList<T> withMakeElementsFrom(AllocationType allocationType, T[] makeElementsFrom, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return HeapObjectIndexList.withArray(allocationType, makeElementsFrom, numElements);
    }

    @Override
    protected IHeapIndexList<T> withHeapMakeElementsFrom(AllocationType allocationType, T[] makeElementsFrom, int numElements) {

        checkWithHeapMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFrom.length, numElements);

        return withMakeElementsFrom(allocationType, makeElementsFrom, numElements);
    }
}
