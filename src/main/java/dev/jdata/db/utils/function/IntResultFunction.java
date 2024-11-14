package dev.jdata.db.utils.function;

@FunctionalInterface
public interface IntResultFunction<T> {

    int apply(T parameter);
}
