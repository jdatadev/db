package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

final class HeapObjectIndexList<T> extends ObjectIndexList<T> implements IHeapIndexList<T> {

    private static final IHeapIndexList<?> emptyList = HeapObjectEmptyIndexList.empty();

    @SuppressWarnings("unchecked")
    static <T> IHeapIndexList<T> empty() {

        return (IHeapIndexList<T>)emptyList;
    }

    static <T> HeapObjectIndexList<T> of(AllocationType allocationType, T instance) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(instance);

        @SuppressWarnings("unchecked")
        final IntFunction<T[]> createElementsArray = l -> (T[])java.lang.reflect.Array.newInstance(instance.getClass(), l);

        return new HeapObjectIndexList<>(allocationType, createElementsArray, instance);
    }

    @SafeVarargs
    static <T> HeapObjectIndexList<T> of(AllocationType allocationType, T ... instances) {

        Objects.requireNonNull(instances);

        return HeapObjectIndexList.withArray(allocationType, Array.copyOf(instances), instances.length);
    }

    private static <T, U extends IObjectIterableElementsView<T> & IOnlyElementsView>
    IHeapIndexList<T> sortedOf(U elements, Comparator<? super T> comparator, IntFunction<T[]> createElementsArray) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(createElementsArray);

        final IHeapIndexList<T> result;

        final AllocationType allocationType = AllocationType.HEAP;

        final int numElements = IOnlyElementsView.intNumElements(elements);

        if (numElements != 0) {

            final HeapMutableObjectIndexList<T> sorted = new HeapMutableObjectIndexList<>(allocationType, createElementsArray, numElements);

            sorted.addTail(elements);

            sorted.sort(comparator);

            result = copyMutableIndexList(allocationType, createElementsArray, sorted);
        }
        else {
            result = empty();
        }

        return result;
    }

    private static <T> HeapObjectIndexList<T> copyArray(AllocationType allocationType, T[] instances, int numElements) {

        checkHeapCopyArrayParameters(allocationType, instances, numElements);

        return new HeapObjectIndexList<>(allocationType, Arrays.copyOf(instances, numElements));
    }

    static <T> HeapObjectIndexList<T> copyArray(AllocationType allocationType, T[] instances, int startIndex, int numElements) {

        checkHeapCopyArrayParameters(allocationType, instances, startIndex, numElements);

        return new HeapObjectIndexList<>(allocationType, startIndex == 0 ? Arrays.copyOf(instances, numElements) : Arrays.copyOfRange(instances, startIndex, numElements));
    }

    static <T> HeapObjectIndexList<T> copyImmutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IIndexList<T> indexList) {

        checkHeapCopyImmutableParameters(allocationType, indexList);
        Objects.requireNonNull(createElementsArray);

        return copyIndexListView(allocationType, createElementsArray, indexList);
    }

    static <T> HeapObjectIndexList<T> copyMutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IMutableIndexList<T> mutableIndexList) {

        checkHeapCopyMutableParameters(allocationType, mutableIndexList);
        Objects.requireNonNull(createElementsArray);

        return copyIndexListView(allocationType, createElementsArray, mutableIndexList);
    }

    static <T> HeapObjectIndexList<T> copyIndexListView(AllocationType allocationType, IntFunction<T[]> createElementsArray, IIndexListView<T> indexList) {

        final HeapObjectIndexList<T> result;

        if (indexList instanceof BaseObjectIndexList) {

            final BaseObjectIndexList<T> baseObjectIndexList = (BaseObjectIndexList<T>)indexList;

            result = new HeapObjectIndexList<T>(allocationType, baseObjectIndexList);
        }
        else {
            result = new HeapObjectIndexList<>(allocationType, createElementsArray, indexList, IOnlyElementsView.intNumElements(indexList));
        }

        return result;
    }

    static <T> HeapObjectIndexList<T> withArray(AllocationType allocationType, T[] instances, int numElements) {

        checkHeapWithArrayParameters(allocationType, instances, numElements);

        return new HeapObjectIndexList<>(allocationType, instances, numElements);
    }

    private HeapObjectIndexList(AllocationType allocationType) {
        super(allocationType);

        AllocationType.checkIsHeap(allocationType);
    }

    private HeapObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    private HeapObjectIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    private HeapObjectIndexList(AllocationType allocationType, T[] instances, int numElements) {
        super(allocationType, instances, numElements);
    }

    private HeapObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IIndexListView<T> toCopy, int numElements) {
        super(allocationType, createElementsArray, toCopy, numElements);
    }

    private HeapObjectIndexList(AllocationType allocationType, BaseObjectIndexList<T> toCopy) {
        super(allocationType, toCopy);
    }

    @Override
    public HeapObjectIndexList<T> toHeapAllocated() {

        return this;
    }
}
