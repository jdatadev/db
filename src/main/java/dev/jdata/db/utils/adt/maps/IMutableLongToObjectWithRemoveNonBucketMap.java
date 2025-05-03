package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLongToObjectWithRemoveNonBucketMap<T>

        extends ILongKeyMap, IClearable, ILongNonContainsMapMutators, ILongToObjectMapMutators<T>, ILongToObjectMapGetters<T> {
}
