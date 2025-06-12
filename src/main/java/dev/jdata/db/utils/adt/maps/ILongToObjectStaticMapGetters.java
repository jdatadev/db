package dev.jdata.db.utils.adt.maps;

interface ILongToObjectStaticMapGetters<T> extends IStaticMapGetters {

    T get(long key);
}
