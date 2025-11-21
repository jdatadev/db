package dev.jdata.db.utils.adt.elements;

public interface IByteContainsOnlyPredicate extends IContainsOnlyPredicateMarker {

    boolean test(byte inputValue, byte listValue);
}
