package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface IObjectUnorderedAddable<T> extends IUnorderedAddable<IObjectIterableElementsView<T>> {

    void addUnordered(T instance);

    default void addUnordered(@SuppressWarnings("unchecked") T ... instances) {

        Checks.isNotEmpty(instances);

        for (T instance : instances) {

            addUnordered(instance);
        }
    }

    @Override
    default void addUnordered(IObjectIterableElementsView<T> elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addUnordered(e));
    }
}
