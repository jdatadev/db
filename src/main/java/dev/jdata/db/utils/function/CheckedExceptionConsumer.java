package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionConsumer<P, E extends Exception> {

    void accept(P parameter) throws E;
}
