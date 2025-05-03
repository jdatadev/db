package dev.jdata.db.utils.adt.maps;

public interface IIntToIntMapRemovalMutators extends IMapMutators {

    int removeAndReturnPrevious(int key, int defaultValue);
}
