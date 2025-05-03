package dev.jdata.db.utils.function;

@FunctionalInterface
public interface ObjIntFunction<T, R> {

    R apply(T object, int i);
}
