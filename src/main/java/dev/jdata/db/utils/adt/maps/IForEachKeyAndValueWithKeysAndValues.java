package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IForEachKeyAndValueWithKeysAndValues<K, V, P1, P2> {

    void each(K keys, long keyIndex, V values, long valueIndex, P1 parameter1, P2 parameter2);
}
