package dev.jdata.db.utils.adt.maps;

interface ILongToObjectBaseStaticMapGetters<T> extends IKeyValueBaseStaticMapGettersMarker {

    T get(long key);
}
