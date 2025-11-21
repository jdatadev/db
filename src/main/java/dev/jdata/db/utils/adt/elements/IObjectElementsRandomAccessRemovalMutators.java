package dev.jdata.db.utils.adt.elements;

import java.util.NoSuchElementException;
import java.util.Objects;

interface IObjectElementsRandomAccessRemovalMutators<T> extends IElementsMutatorsMarker {

    boolean removeAtMostOne(T element);

    boolean removeAtMostOneInstance(T instance);

    default void removeExactlyOne(T element) {

        Objects.requireNonNull(element);

        if (!removeAtMostOne(element)) {

            throw ElementsExceptions.moreThanOneFoundException();
        }
    }

    default void removeExactlyOneInstance(T instance) {

        Objects.requireNonNull(instance);

        if (!removeAtMostOneInstance(instance)) {

            throw new NoSuchElementException();
        }
    }
}
