package dev.jdata.db.utils.adt.elements;

public interface IByteElementPredicate<P> extends IElementPredicateMarker {

    boolean test(byte value, P parameter);
}
