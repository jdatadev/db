package dev.jdata.db.utils.adt.maps;

interface ILongToIntDynamicMapGetters extends IDynamicMapGetters {

    int get(long key, int defaultValue);
}
