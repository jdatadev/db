package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLongToObjectStaticMap<T>

        extends ILongKeyMap, IClearable, ILongKeyStaticMapRemovalMutators, ILongToObjectCommonMapMutators<T>, ILongToObjectCommonMapGetters<T> {
}
