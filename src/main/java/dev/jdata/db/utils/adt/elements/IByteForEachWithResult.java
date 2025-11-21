package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IByteForEachWithResult<P1, P2, R, E extends Exception> extends IForEachElementsWithResultMarker {

    R each(byte element, P1 parameter, P2 parameter2) throws E;
}
