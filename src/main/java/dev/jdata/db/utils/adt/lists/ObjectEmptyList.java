package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.ObjectEmptyOrderedOnlyElements;

abstract class ObjectEmptyList<T> extends ObjectEmptyOrderedOnlyElements<T> implements IListView<T> {

    @Override
    public final T getHead() {

        throw ElementsExceptions.emptyException();
    }

    @Override
    public final T getTail() {

        throw ElementsExceptions.emptyException();
    }
}
