package dev.jdata.db.utils.adt.elements;

public interface IIntElements extends IElements {

    @FunctionalInterface
    public interface IIntElementPredicate<P> {

        boolean test(int value, P parameter);
    }

    boolean contains(int value);

    boolean containsOnly(int value);

    @FunctionalInterface
    public interface IContainsOnlyPredicate {

        boolean test(int inputValue, int elementsValue);
    }

    boolean containsOnly(int value, IContainsOnlyPredicate predicate);

    int[] toArray();
}
