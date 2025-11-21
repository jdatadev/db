package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionByteConsumer<E extends Exception> {

    void accept(byte parameter) throws E;
}
