package dev.jdata.db.utils.adt.elements;

public interface IIntUnorderedElementsMutators {

    void add(int value);

    default void addAll(IIntIterableElements intElements) {

        intElements.forEach(this, (e, i) -> i.add(e));
    }
}
