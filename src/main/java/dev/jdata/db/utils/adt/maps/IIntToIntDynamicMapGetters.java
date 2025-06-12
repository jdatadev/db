package dev.jdata.db.utils.adt.maps;

interface IIntToIntDynamicMapGetters extends IDynamicMapGetters {

    int get(int key, int defaultValue);
}
