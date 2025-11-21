package dev.jdata.db.utils.adt.maps;

interface ILongToIntDynamicMapGetters extends IKeyValueDynamicMapGettersMarker {

    int get(long key, int defaultValue);
}
