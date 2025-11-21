package dev.jdata.db.utils.adt.elements;

import java.util.Comparator;
import java.util.Objects;

interface IObjectOrderedElementsMutable<T>

        extends IOrderedElementsMutable<Comparator<? super T>>, IObjectOrderedElementsMutators<T>, IObjectOrderedAddTailElementsMutators<T>, IObjectAnyOrderAddable<T> {

    @Override
    default void addInAnyOrder(T instance) {

        Objects.requireNonNull(instance);

        addTail(instance);
    }
}
