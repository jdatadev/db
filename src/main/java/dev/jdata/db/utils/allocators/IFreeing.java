package dev.jdata.db.utils.allocators;

public interface IFreeing<T> {

    void free(T instance);
}
