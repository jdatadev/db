package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableIntToObjectWithRemoveNonBucketMap<T> extends IClearable, IIntToObjectMapMutators<T>, IIntNonContainsMapMutators, IIntToObjectMapGetters<T> {

    T removeAndReturnPrevious(int key);
}
