package dev.jdata.db.utils.adt.maps;

public interface IntKeyMap extends KeyMap<int[]> {

    boolean containsKey(int key);

    boolean remove(int key);
}
