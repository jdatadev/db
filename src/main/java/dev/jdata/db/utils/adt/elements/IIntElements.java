package dev.jdata.db.utils.adt.elements;

public interface IIntElements {

    @FunctionalInterface
    public interface IntElementPredicate<P> {

        boolean test(int value, P parameter);
    }

    @FunctionalInterface
    public interface ForEach<P, E extends Exception> {

        void each(int element, P parameter) throws E;
    }

    <P, E extends Exception> void forEach(P parameter, ForEach<P, E> forEach) throws E;

    boolean contains(int value);
}
