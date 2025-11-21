package dev.jdata.db.utils.adt.elements;

interface IIntElementsRandomAccessRemovalMutators extends IElementsMutatorsMarker {

    boolean removeAtMostOne(int element);

    default void removeExactlyOne(int element) {

        if (!removeAtMostOne(element)) {

            throw ElementsExceptions.moreThanOneFoundException();
        }
    }
}
