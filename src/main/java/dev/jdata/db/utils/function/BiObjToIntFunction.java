package dev.jdata.db.utils.function;

@FunctionalInterface
public interface BiObjToIntFunction<T, U> {

    int apply(T object1, U object2);
}
