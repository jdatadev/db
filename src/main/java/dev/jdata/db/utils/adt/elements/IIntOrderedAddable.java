package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public interface IIntOrderedAddable extends IOrderedAddable<IIntIterableElementsView> {

    void addTail(int value);

    @Override
    default void addTail(IIntIterableElementsView elements) {

        Objects.requireNonNull(elements);

        elements.forEach(this, (e, i) -> i.addTail(e));
    }

    default void addTail(int ... values) {

        Checks.isNotEmpty(values);

        for (int value : values) {

            addTail(value);
        }
    }
}
