package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface ILongOrderedAddable extends IOrderedAddable<ILongIterableElementsView> {

    void addTail(long value);

    @Override
    default void addTail(ILongIterableElementsView elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, i) -> i.addTail(e));
    }

    default void addTail(long ... values) {

        Checks.isNotEmpty(values);

        for (long value : values) {

            addTail(value);
        }
    }
}
