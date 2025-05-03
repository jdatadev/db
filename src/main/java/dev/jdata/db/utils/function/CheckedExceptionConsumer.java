package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionConsumer<T, E extends Exception> {

    void accept(T parameter) throws E;
}
