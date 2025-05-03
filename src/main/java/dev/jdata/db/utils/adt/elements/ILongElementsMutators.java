package dev.jdata.db.utils.adt.elements;

public interface ILongElementsMutators {

    void add(long value);

    default void addAll(ILongElements longElements) {

        longElements.forEach(this, (e, i) -> i.add(e));
    }

    boolean remove(long value);
}
