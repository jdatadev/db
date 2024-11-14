package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionFunction<T, R, E extends Exception> {

    R apply(T parameter) throws E;
}
