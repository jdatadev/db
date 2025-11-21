package dev.jdata.db.utils.adt.elements;

public interface IByteElementsGetters extends IElementsGettersMarker {

    boolean contains(byte value);
    <P> boolean contains(P parameter, IByteElementPredicate<P> predicate);

    boolean containsOnly(byte value);
    boolean containsOnly(byte value, IByteContainsOnlyPredicate predicate);

    byte[] toArray();
}
