package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IIntContainsOnlyPredicate extends IContainsOnlyPredicateMarker {

    boolean test(int inputValue, int elementsValue);
}
