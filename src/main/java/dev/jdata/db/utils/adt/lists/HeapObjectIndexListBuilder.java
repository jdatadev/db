package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

final class HeapObjectIndexListBuilder<T>

        extends ObjectIndexListBuilder<T, IHeapIndexList<T>, IHeapIndexList<T>, MutableObjectIndexList<T>, IHeapIndexListBuilder<T>>
        implements IHeapIndexListBuilder<T> {

    HeapObjectIndexListBuilder(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        this(allocationType, createElementsArray, DEFAULT_INITIAL_CAPACITY);
    }

    HeapObjectIndexListBuilder(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        this(allocationType, initialCapacity, new HeapObjectIndexListAllocator<>(createElementsArray), (t, c, a) -> a.allocate(t, c));
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
    protected IHeapIndexList<T> withArray(AllocationType allocationType, T[] elementsArray, int numElements) {

        checkWithArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return HeapObjectIndexList.withArray(allocationType, elementsArray, numElements);
    }

    @Override
    protected IHeapIndexList<T> withHeapArray(AllocationType allocationType, T[] elementsArray, int numElements) {

        checkWithHeapArrayParameters(allocationType, elementsArray, elementsArray.length, numElements);

        return withArray(allocationType, elementsArray, numElements);
    }
}
