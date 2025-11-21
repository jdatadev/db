package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IIntElementPredicate<P> extends IElementPredicateMarker {

    boolean test(int value, P parameter);
}
