package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

interface ILongUnorderedAddable extends IUnorderedAddable<ILongIterableElementsView> {

    void addUnordered(long value);

    default void addUnordered(long ... values) {

        Checks.isNotEmpty(values);

        for (long value : values) {

            addUnordered(value);
        }
    }

    @Override
    default void addUnordered(ILongIterableElementsView elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addUnordered(e));
    }
}
