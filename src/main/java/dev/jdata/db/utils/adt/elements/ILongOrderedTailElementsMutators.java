package dev.jdata.db.utils.adt.elements;

public interface ILongOrderedTailElementsMutators {

    void addTail(long value);

    default void addTailAll(ILongIterableElements longElements) {

        longElements.forEach(this, (e, i) -> i.addTail(e));
    }
}
