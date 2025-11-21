package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntToIntForEachMapKeyAndValue<P, E extends Exception> extends IForEachMapKeyAndValueMarker {

    void each(int key, int value, P parameter) throws E;
}
