package dev.jdata.db.utils.adt.elements;

public interface ILongElements {

    @FunctionalInterface
    public interface ILongElementPredicate<P> {

        boolean test(long value, P parameter);
    }

    boolean contains(long value);

    boolean containsOnly(long value);

    @FunctionalInterface
    public interface IContainsOnlyPredicate {

        boolean test(long inputValue, long listValue);
    }

    boolean containsOnly(long value, IContainsOnlyPredicate predicate);

    long[] toArray();
}
