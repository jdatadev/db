package dev.jdata.db.utils.adt.elements;

public interface ILongElements {

    @FunctionalInterface
    public interface LongElementPredicate<P> {

        boolean test(long value, P parameter);
    }

    @FunctionalInterface
    public interface ForEach<P, E extends Exception> {

        void each(long element, P parameter) throws E;
    }

    <P, E extends Exception> void forEach(P parameter, ForEach<P, E> forEach) throws E;

    boolean contains(long value);
}
