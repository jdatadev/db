package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IObjectContainsOnlyPredicate<T> extends IContainsOnlyPredicateMarker {

    boolean test(T inputValue, T elementsValue);
}
