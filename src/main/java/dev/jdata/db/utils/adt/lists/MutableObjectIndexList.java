package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.checks.Checks;

abstract class MutableObjectIndexList<T> extends BaseObjectIndexList<T> implements IMutableIndexList<T> {

    MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    MutableObjectIndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    <U> MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseObjectIndexList<U> toCopy, Function<U,T> mapper) {
        super(allocationType, createElementsArray, toCopy, IOnlyElementsView.intNumElements(toCopy), mapper);
    }

    private MutableObjectIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IBaseObjectIndexList<T> toCopy) {
        super(allocationType, createElementsArray, toCopy, IOnlyElementsView.intNumElements(toCopy));
    }

    @Override
    public final long getCapacity() {

        return getElementsCapacity();
    }

    @Override
    public final void clear() {

        clearElements();
    }

    @Override
    public final void addTail(T instance) {

        Objects.requireNonNull(instance);

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
            IMutableIndexList.super.addTail(elements);
        }
    }

    @Override
    public final T setAndReturnPrevious(long index, T value) {

        Checks.checkLongIndex(index, getNumElements());
        Objects.requireNonNull(value);

        final int intIndex = intIndex(index);

        final T[] elementsArray = getElementsArray();

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

        decrementNumElements();;

        return result;
    }
}
