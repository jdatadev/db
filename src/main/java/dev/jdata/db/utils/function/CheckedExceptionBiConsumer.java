package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionBiConsumer<P1, P2, E extends Exception> {

    void accept(P1 parameter1, P2 parameter2) throws E;
}
