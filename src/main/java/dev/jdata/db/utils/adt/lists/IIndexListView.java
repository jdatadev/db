package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.IObjectByIndexOrderedElementsView;

public interface IIndexListView<T> extends IListView<T>, IObjectByIndexOrderedElementsView<T> {

    @Override
    default T[] toArray(IntFunction<T[]> createArray) {

        Objects.requireNonNull(createArray);

        return IObjectByIndexOrderedElementsView.super.toArray(createArray);
    }

    @Override
    default T getHead() {

        if (isEmpty()) {

            throw ElementsExceptions.emptyException();
        }

        return get(0L);
    }

    @Override
    default T getTail() {

        if (isEmpty()) {

            throw ElementsExceptions.emptyException();
        }

        return get(getNumElements() - 1L);
    }
}

