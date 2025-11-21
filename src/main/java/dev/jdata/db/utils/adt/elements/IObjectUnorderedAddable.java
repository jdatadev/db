package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface IObjectUnorderedAddable<T> extends IUnorderedAddable<IObjectIterableElementsView<T>> {

    void addUnordered(T instance);

    @Override
    default void addUnordered(IObjectIterableElementsView<T> elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addUnordered(e));
    }

    default void addUnordered(@SuppressWarnings("unchecked") T ... instances) {

        Checks.isNotEmpty(instances);

        for (T instance : instances) {

            addUnordered(instance);
        }
    }

    default void addUnordered(T[] instances, int numElements) {

        Checks.isNotEmpty(instances);
        Checks.checkNumElements(instances, numElements);

        addUnordered(instances, 0, numElements);
    }

    default void addUnordered(T[] instances, int startIndex, int numElements) {

        Checks.isNotEmpty(instances);
        Checks.checkIntAddFromArray(instances, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addUnordered(instances[i]);
        }
    }
}
