package dev.jdata.db.utils.adt.lists;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

public final class HeapIndexList<T> extends IndexList<T> implements IHeapIndexList<T> {

    @SafeVarargs
    public static <T> HeapIndexList<T> of(T ... instances) {

        Objects.requireNonNull(instances);

        return new HeapIndexList<>(AllocationType.HEAP, Array.copyOf(instances));
    }

    private static final HeapIndexList<?> emptyList = new HeapIndexList<>(AllocationType.HEAP);

    @SuppressWarnings("unchecked")
    public static <T> HeapIndexList<T> empty() {

        return (HeapIndexList<T>)emptyList;
    }

    public static <T> HeapIndexList<T> of(T instance) {

        Objects.requireNonNull(instance);

        @SuppressWarnings("unchecked")
        final IntFunction<T[]> createElementsArray = l -> (T[])java.lang.reflect.Array.newInstance(instance.getClass(), l);

        return new HeapIndexList<>(AllocationType.HEAP, createElementsArray, instance);
    }

    public static <T> HeapIndexListBuilder<T> createBuilder(IntFunction<T[]> createElementsArray) {

        return createBuilder(createElementsArray, DEFAULT_INITIAL_CAPACITY);
    }

    public static <T> HeapIndexListBuilder<T> createBuilder(IntFunction<T[]> createElementsArray, int initialCapacity) {

        Objects.requireNonNull(createElementsArray);
        Checks.isCapacity(initialCapacity);

        return new HeapIndexListBuilder<>(AllocationType.HEAP, createElementsArray, initialCapacity);
    }

    public static <T> HeapIndexList<T> sortedOf(IObjectIterableElementsView<T> elements, Comparator<? super T> comparator, IntFunction<T[]> createElementsArray) {

        final HeapIndexList<T> result;

        final int numElements = IElementsView.intNumElements(elements.getNumElements());

        if (numElements != 0) {

            final HeapMutableIndexList<T> sorted = HeapMutableIndexList.create(createElementsArray, numElements);

            sorted.addTail(elements);

            sorted.sort(comparator);

            result = fromMutableIndexList(AllocationType.HEAP, sorted);
        }
        else {
            result = HeapIndexList.empty();
        }

        return result;
    }

    static <T> HeapIndexList<T> fromMutableIndexList(AllocationType allocationType, MutableObjectIndexList<T, ?, ?, ?> mutableIndexList) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(mutableIndexList);

        return new HeapIndexList<>(allocationType, mutableIndexList.makeArrayCopy());
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

    HeapIndexList(MutableObjectIndexList<T, ?, ?, ?> toCopy) {
        super(toCopy);
    }

    @Override
    public HeapIndexList<T> toHeapAllocated() {

        return this;
    }
}
