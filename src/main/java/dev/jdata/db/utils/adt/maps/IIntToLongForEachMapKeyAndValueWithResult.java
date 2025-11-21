package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntToLongForEachMapKeyAndValueWithResult<P1, P2, R, E extends Exception> extends IForEachMapKeyAndValueWithResultMarker {

    R each(int key, long value, P1 parameter1, P2 parameter2) throws E;
}
