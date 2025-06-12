package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIterableElements;

public interface IListBuildable<T> {

    void addTail(T instance);

    default void addTail(IIterableElements<T> instances) {

        instances.forEach(this, (e, b) -> b.addTail(e));
    }

    void addTail(@SuppressWarnings("unchecked") T ... instances);
}
