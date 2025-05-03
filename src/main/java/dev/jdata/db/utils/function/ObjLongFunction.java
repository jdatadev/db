package dev.jdata.db.utils.function;

@FunctionalInterface
public interface ObjLongFunction<T, R> {

    R apply(T object, long l);
}
