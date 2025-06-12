package dev.jdata.db.utils.adt.elements;

public interface IAddable<T> extends IMutable {

    void add(T instance);

    void add(@SuppressWarnings("unchecked") T ... instances);
}
