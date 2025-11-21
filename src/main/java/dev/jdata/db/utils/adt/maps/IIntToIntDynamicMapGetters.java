package dev.jdata.db.utils.adt.maps;

interface IIntToIntDynamicMapGetters extends IKeyValueDynamicMapGettersMarker {

    int get(int key, int defaultValue);
}
