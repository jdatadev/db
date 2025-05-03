package dev.jdata.db.utils.adt.maps;

public interface IIntToIntBucketMapGetters extends IMapGetters {

    int get(int key, int defaultValue);
}
