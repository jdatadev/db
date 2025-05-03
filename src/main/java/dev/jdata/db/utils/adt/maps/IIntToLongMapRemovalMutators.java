package dev.jdata.db.utils.adt.maps;

public interface IIntToLongMapRemovalMutators extends IMapMutators {

    long removeAndReturnPrevious(int key, long defaultValue);
}
