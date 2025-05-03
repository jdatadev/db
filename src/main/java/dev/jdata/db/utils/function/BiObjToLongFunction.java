package dev.jdata.db.utils.function;

@FunctionalInterface
public interface BiObjToLongFunction<T, U> {

    long apply(T object1, U object2);
}
