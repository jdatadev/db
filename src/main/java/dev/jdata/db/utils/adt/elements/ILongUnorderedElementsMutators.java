package dev.jdata.db.utils.adt.elements;

public interface ILongUnorderedElementsMutators {

    void add(long value);

    default void addAll(ILongIterableElements longElements) {

        longElements.forEach(this, (e, i) -> i.add(e));
    }
}
