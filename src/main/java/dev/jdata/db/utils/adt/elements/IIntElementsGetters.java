package dev.jdata.db.utils.adt.elements;

interface IIntElementsGetters extends IElementsGettersMarker {

    boolean contains(int value);
    <P> boolean contains(P parameter, IIntElementPredicate<P> predicate);

    boolean containsOnly(int value);
    boolean containsOnly(int value, IIntContainsOnlyPredicate predicate);
}
