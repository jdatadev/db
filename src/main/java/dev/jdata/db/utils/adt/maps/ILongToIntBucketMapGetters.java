package dev.jdata.db.utils.adt.maps;

public interface ILongToIntBucketMapGetters extends IMapGetters {

    int get(long key, int defaultValue);
}
