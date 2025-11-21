package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface ILongContainsOnlyPredicate extends IContainsOnlyPredicateMarker {

    boolean test(long inputValue, long listValue);
}
