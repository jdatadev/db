package dev.jdata.db.utils.adt.arrays;

public interface IObjectArrayMutators<T> {

    void add(T value);

    void set(long index, T value);
}
