package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface ElementGetter<T, E> {

    E getElement(T byIndex, int index);
}
