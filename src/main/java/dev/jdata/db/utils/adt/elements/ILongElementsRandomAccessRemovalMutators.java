package dev.jdata.db.utils.adt.elements;

interface ILongElementsRandomAccessRemovalMutators extends IElementsMutatorsMarker {

    boolean removeAtMostOne(long element);

    default void removeExactlyOne(long element) {

        if (!removeAtMostOne(element)) {

            throw ElementsExceptions.moreThanOneFoundException();
        }
    }
}
