package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface IObjectOrderedAddable<T> extends IOrderedAddable<IObjectIterableElementsView<T>> {

    void addTail(T instance);

    @Override
    default void addTail(IObjectIterableElementsView<T> elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addTail(e));
    }

    default void addTail(@SuppressWarnings("unchecked") T ... instances) {

        Checks.isNotEmpty(instances);

        for (T instance : instances) {

            addTail(instance);
        }
    }
}
