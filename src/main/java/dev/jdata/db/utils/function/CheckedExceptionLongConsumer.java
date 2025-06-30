package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionLongConsumer<E extends Exception> {

    void accept(long parameter) throws E;
}
