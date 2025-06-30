package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElements;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.InstanceAllocator;

public abstract class MutableIndexList<T> extends BaseIndexList<T> implements IMutableIndexList<T> {

    static abstract class MutableIndexListAllocator<T, U extends MutableIndexList<T>> extends InstanceAllocator<T> {

        abstract U allocateMutableIndexList(int minimumCapacity);
        abstract void freeMutableIndexList(U list);
    }

    @SafeVarargs
    public static <T> HeapMutableIndexList<T> of(T ... instances) {

        Objects.requireNonNull(instances);

        return new HeapMutableIndexList<>(AllocationType.HEAP, instances);
    }

    private MutableIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    MutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    MutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    MutableIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    MutableIndexList(IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {
        super(createElementsArray, toCopy);
    }

    private MutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {
        super(allocationType, createElementsArray, toCopy);
    }

    @Override
    public final long getCapacity() {

        return elementsArray.length;
    }

    @Override
    public final void addHead(T instance) {

        addHeadElement(instance);
    }

    @Override
    public final void addTail(T instance) {

        addTailElement(instance);
    }

    @Override
    public final void addTail(@SuppressWarnings("unchecked") T... instances) {

        addTailElements(instances);
    }

    @Override
    public final void addTail(IObjectIterableElements<T> elements) {

        if (elements instanceof BaseObjectArrayList<?>) {

            @SuppressWarnings("unchecked")
            final BaseObjectArrayList<T> baseArrayList = (BaseObjectArrayList<T>)elements;

            addTail(baseArrayList);
        }
        else {
            IMutableIndexList.super.addTail(elements);
        }
    }

    public final <U extends IndexList<T>> U copyToImmutable(IndexListAllocator<T, U, ?, ?> indexListAllocator) {

        Objects.requireNonNull(indexListAllocator);

        return indexListAllocator.copyToImmutable(this);
    }

    final <P, R> R makeFromElementsAndDispose(P parameter, MakeFromElementsFunction<T[], P, R> makeFromElements) {

        final R result = makeFromElements(parameter, makeFromElements);

        resetToNullAndDispose();

        return result;
    }

    final <P, R> R makeFromElementsAndRecreate(P parameter, MakeFromElementsFunction<T[], P, R> makeFromElements) {

        final R result = makeFromElements(parameter, makeFromElements);

        recreateArrays();

        return result;
    }
}
