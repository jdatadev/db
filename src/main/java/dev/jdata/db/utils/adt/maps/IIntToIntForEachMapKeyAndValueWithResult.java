package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntToIntForEachMapKeyAndValueWithResult<P1, P2, R, E extends Exception> extends IForEachMapKeyAndValueWithResultMarker {

    R each(int key, int value, P1 parameter1, P2 parameter2) throws E;
}
