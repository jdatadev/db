package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntToLongForEachMapKeyAndValue<P, E extends Exception> extends IForEachMapKeyAndValueMarker {

    void each(int key, long value, P parameter) throws E;
}
