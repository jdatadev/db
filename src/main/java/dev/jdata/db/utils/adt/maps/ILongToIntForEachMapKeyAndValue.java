package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface ILongToIntForEachMapKeyAndValue<P, E extends Exception> extends IForEachMapKeyAndValueMarker {

    void each(long key, int value, P parameter) throws E;
}
