package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.marker.IAddable;
import dev.jdata.db.utils.checks.Checks;

interface IUnorderedAddable<T extends IElementsIterable> extends IAddable {

    void addUnordered(T elements);

    default void addUnordered(T[] instances) {

        Checks.isNotEmpty(instances);

        for (T instance : instances) {

            addUnordered(instance);
        }
    }

    default void addUnordered(T[] instances, int numElements) {

        Checks.checkNumElements(instances, numElements);

        addUnordered(instances, 0, numElements);
    }

    default void addUnordered(T[] instances, int startIndex, int numElements) {

        Checks.checkIntAddFromArray(instances, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addUnordered(instances[i]);
        }
    }
}
