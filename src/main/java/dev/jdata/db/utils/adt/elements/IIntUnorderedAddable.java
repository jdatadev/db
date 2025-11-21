package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface IIntUnorderedAddable extends IUnorderedAddable<IIntIterableOnlyElementsView> {

    void addUnordered(int value);

    @Override
    default void addUnordered(IIntIterableOnlyElementsView elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addUnordered(e));
    }

    default void addUnordered(int ... values) {

        Checks.isNotEmpty(values);

        for (int value : values) {

            addUnordered(value);
        }
    }

    default void addUnordered(int[] values, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkNumElements(values, numElements);

        addUnordered(values, 0, numElements);
    }

    default void addUnordered(int[] values, int startIndex, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkIntAddFromArray(values, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addUnordered(values[i]);
        }
    }
}
