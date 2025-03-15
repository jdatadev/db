package dev.jdata.db.utils.function;

@FunctionalInterface
public interface ByteGetter<T> {

    byte get(T instance, int index);
}
