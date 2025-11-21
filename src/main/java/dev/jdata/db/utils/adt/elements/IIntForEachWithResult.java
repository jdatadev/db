package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IIntForEachWithResult<P1, P2, R, E extends Exception> extends IForEachElementsWithResultMarker {

    R each(int element, P1 parameter, P2 parameter2) throws E;
}
