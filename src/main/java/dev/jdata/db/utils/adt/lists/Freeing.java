package dev.jdata.db.utils.adt.lists;

public interface Freeing<T> {

    void free(T instance);
}
