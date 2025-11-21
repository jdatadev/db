package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

import dev.jdata.db.utils.adt.elements.ObjectEmptyOrderedOnlyElements;

abstract class ObjectEmptyList<T> extends ObjectEmptyOrderedOnlyElements<T> implements IListView<T> {

    @Override
    public final T getHead() {

        throw new NoSuchElementException();
    }

    @Override
    public final T getTail() {

        throw new NoSuchElementException();
    }
}
