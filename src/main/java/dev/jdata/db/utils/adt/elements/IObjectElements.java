package dev.jdata.db.utils.adt.elements;

public interface IObjectElements<T> extends IElements {

    @FunctionalInterface
    public interface IObjectElementPredicate<T, P> {

        boolean test(T value, P parameter);
    }

    @FunctionalInterface
    public interface IContainsOnlyPredicate<T> {

        boolean test(T inputValue, T elementsValue);
    }
}
