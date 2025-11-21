package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface ILongUnorderedAddable extends IUnorderedAddable<ILongIterableOnlyElementsView> {

    void addUnordered(long value);

    @Override
    default void addUnordered(ILongIterableOnlyElementsView elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addUnordered(e));
    }

    default void addUnordered(long ... values) {

        Checks.isNotEmpty(values);

        for (long value : values) {

            addUnordered(value);
        }
    }

    default void addUnordered(long[] values, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkNumElements(values, numElements);

        addUnordered(values, 0, numElements);
    }

    default void addUnordered(long[] values, int startIndex, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkIntAddFromArray(values, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addUnordered(values[i]);
        }
    }
}
