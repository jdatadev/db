package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface ILongElementPredicate<P> extends IElementPredicateMarker {

    boolean test(long value, P parameter);
}
