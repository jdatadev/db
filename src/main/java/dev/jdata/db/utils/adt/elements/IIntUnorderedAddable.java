package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

interface IIntUnorderedAddable extends IUnorderedAddable<IIntIterableElementsView> {

    void addUnordered(int value);

    default void addUnordered(int ... values) {

        Checks.isNotEmpty(values);

        for (int value : values) {

            addUnordered(value);
        }
    }

    @Override
    default void addUnordered(IIntIterableElementsView elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, t) -> t.addUnordered(e));
    }
}
