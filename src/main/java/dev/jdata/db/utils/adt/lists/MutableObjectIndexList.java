package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

abstract class MutableObjectIndexList<

                T,
                IMMUTABLE extends IIndexList<T>,
                BUILDER extends IIndexListBuilder<T, IMMUTABLE, ?>,
                ALLOCATOR extends IIndexListAllocator<T, IMMUTABLE, BUILDER>>

        extends BaseIndexList<T>
        implements IMutableIndexList<T, IMMUTABLE, ALLOCATOR> {
/*
    static abstract class MutableObjectIndexListAllocator<T, U extends MutableObjectIndexList<T>> extends DelegatingInstanceAllocator<T> implements imutableinde{

        abstract U allocateMutableIndexList(int minimumCapacity);
        public abstract void freeMutableIndexList(U list);
    }
*/
    @SafeVarargs
    private static <T> HeapMutableIndexList<T> of(T ... instances) {

        Objects.requireNonNull(instances);

        return new HeapMutableIndexList<>(AllocationType.HEAP, instances);
    }

    private static <T, U extends MutableObjectIndexList<T>, V extends MutableObjectIndexListAllocator<T, U>> U create(int minimumCapacity, V listAllocator) {

        Checks.isCapacity(minimumCapacity);
        Objects.requireNonNull(listAllocator);

        return listAllocator.allocateMutableIndexList(minimumCapacity);
    }

    private MutableObjectIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    MutableObjectIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    <U> MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseIndexList<U> toCopy, Function<U,T> mapper) {
        super(allocationType, createElementsArray, toCopy, mapper);
    }

    private MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseIndexList<T> toCopy) {
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

        Checks.isNotEmpty(instances);

        addTailElements(instances);
    }

    @Override
    public final void addTail(IObjectIterableElementsView<T> elements) {

        Objects.requireNonNull(elements);

        if (elements instanceof BaseObjectArrayList<?>) {

            @SuppressWarnings("unchecked")
            final BaseObjectArrayList<T> baseArrayList = (BaseObjectArrayList<T>)elements;

            addTail(baseArrayList);
        }
        else {
            IBaseMutableIndexList.super.addTail(elements);
        }
    }

    @Override
    public final T setAndReturnPrevious(long index, T value) {

        Checks.checkIndex(index, getNumElements());
        Objects.requireNonNull(value);

        final int intIndex = IByIndexView.intIndex(index);

        final T result = elementsArray[intIndex];

        elementsArray[intIndex] = value;

        return result;
    }

    @Override
    public final T removeTailAndReturnValue() {

        if (isEmpty()) {

            throw new IllegalStateException();
        }

        final T result = getTail();

        -- numElements;

        return result;
    }

    public final <U extends IndexList<T>> U copyToImmutable(IndexListAllocator<T, U, ?, ?, BUILDER, BUILDER_ALLOCATOR> indexListAllocator) {

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
