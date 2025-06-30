package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionIntConsumer<E extends Exception> {

    void accept(int parameter) throws E;
}
