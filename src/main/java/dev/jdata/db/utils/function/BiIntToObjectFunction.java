package dev.jdata.db.utils.function;

@FunctionalInterface
public interface BiIntToObjectFunction<T> {

    T apply(int parameter1, int parameter2);
}
