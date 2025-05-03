package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableIntToLongWithRemoveNonBucketMap extends IIntToLongMap, IClearable, IIntNonContainsMapMutators, IIntToLongMapMutators,
        IIntToLongNonContainsNonBucketMapGetters {

    long removeAndReturnPrevious(int key);
}
