package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectIterableElements;

public interface IListBuildable<T, V extends IListBuildable<T, V>> {

    V addTail(T instance);

    V addTail(@SuppressWarnings("unchecked") T ... instances);

    default V addTail(IObjectIterableElements<T> instances) {

        instances.forEach(this, (e, b) -> b.addTail(e));

        @SuppressWarnings("unchecked")
        final V result = (V)this;

        return result;
    }
}
