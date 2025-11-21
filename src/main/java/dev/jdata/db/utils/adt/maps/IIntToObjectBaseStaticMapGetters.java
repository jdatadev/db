package dev.jdata.db.utils.adt.maps;

interface IIntToObjectBaseStaticMapGetters<T> extends IKeyValueBaseStaticMapGettersMarker {

    T get(int key);
}
