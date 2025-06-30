package dev.jdata.db.utils.adt.elements;

public interface IObjectUnorderedElementsMutators<T> {

    void add(T instance);

    default void addAll(IObjectIterableElements<T> elements) {

        elements.forEach(this, (e, i) -> i.add(e));
    }
}
