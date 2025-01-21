package dev.jdata.db.utils.adt.maps;

public interface LongKeyMap extends KeyMap<long[]> {

    boolean containsKey(long key);

    boolean remove(long key);
}
