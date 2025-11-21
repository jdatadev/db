package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

interface IObjectElementsRandomAccessRemovalMutators<T> extends IElementsMutatorsMarker {

    boolean removeAtMostOne(T element);

    default void removeExactlyOne(T element) {

        Objects.requireNonNull(element);

        if (!removeAtMostOne(element)) {

            throw ElementsExceptions.moreThanOneFoundException();
        }
    }
}
