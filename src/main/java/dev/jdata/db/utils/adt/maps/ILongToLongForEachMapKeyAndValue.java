package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface ILongToLongForEachMapKeyAndValue<P, E extends Exception> extends IForEachMapKeyAndValueMarker {

    void each(long key, long value, P parameter) throws E;
}
