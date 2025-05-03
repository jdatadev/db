package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionBiConsumer<T, U, E extends Exception> {

    void accept(T parameter1, U parameter2) throws E;
}
