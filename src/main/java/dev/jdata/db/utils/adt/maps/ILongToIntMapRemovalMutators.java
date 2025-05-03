package dev.jdata.db.utils.adt.maps;

public interface ILongToIntMapRemovalMutators extends IMapMutators {

    int removeAndReturnPrevious(long key, int defaultValue);
}
