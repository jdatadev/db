package dev.jdata.db.utils.adt.maps;

interface IIntToObjectStaticMapGetters<T> extends IStaticMapGetters {

    T get(int key);
}
