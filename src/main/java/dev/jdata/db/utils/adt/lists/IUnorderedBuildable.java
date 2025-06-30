package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IElementsBuildable;
import dev.jdata.db.utils.adt.elements.IObjectIterableElements;

public interface IUnorderedBuildable<T, V> extends IElementsBuildable {

    V add(T instance);

    V add(@SuppressWarnings("unchecked") T ... instances);

    default V add(IObjectIterableElements<T> instances) {

        instances.forEach(this, (e, b) -> b.add(e));

        @SuppressWarnings("unchecked")
        final V result = (V)this;

        return result;
    }
}
