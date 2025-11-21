package dev.jdata.db.utils.adt.elements;

interface ILongElementsGetters extends IElementsGettersMarker {

    boolean contains(long value);
    <P> boolean contains(P parameter, ILongElementPredicate<P> predicate);

    boolean containsOnly(long value);
    boolean containsOnly(long value, ILongContainsOnlyPredicate predicate);

    long[] toArray();
}
