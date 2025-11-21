package dev.jdata.db.utils.adt.elements;

@FunctionalInterface
public interface IObjectForEachWithResult<T, P1, P2, R, E extends Exception> extends IForEachElementsWithResultMarker {

    R each(T element, P1 parameter1, P2 parameter2) throws E;
}
