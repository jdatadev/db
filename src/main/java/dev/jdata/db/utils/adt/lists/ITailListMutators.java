package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectIterableElements;

public interface ITailListMutators<T> {

    void addTail(T instance);

    default void addTail(IObjectIterableElements<T> elements) {

        elements.forEach(this, (e, t) -> t.addTail(e));
    }

    void addTail(@SuppressWarnings("unchecked") T ... instances);
}
