package dev.jdata.db.utils.function;

@FunctionalInterface
public interface CheckedExceptionFunction<P, R, E extends Exception> {

    R apply(P parameter) throws E;
}
