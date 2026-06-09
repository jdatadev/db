package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

interface IObjectHeadListMutators<T> extends IHeadListMutators {

    void addHead(T instance);

    T removeHeadAndReturnValue();

    default void addHead(IObjectIterableElementsView<? extends T> elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addHead(e));
    }

    default void addHead(@SuppressWarnings("unchecked") T ... instances) {

        Checks.isNotEmpty(instances);

        for (T instance : instances) {

            addHead(instance);
        }
    }
}
