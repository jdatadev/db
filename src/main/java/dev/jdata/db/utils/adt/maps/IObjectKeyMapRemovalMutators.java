package dev.jdata.db.utils.adt.maps;

public interface IObjectKeyMapRemovalMutators<T> extends IMapMutators {

    boolean remove(T key);
}
