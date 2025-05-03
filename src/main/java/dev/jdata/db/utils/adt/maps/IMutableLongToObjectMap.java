package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLongToObjectMap<T>

        extends ILongKeyMap, IClearable, ILongKeyMapRemovalMutators, ILongToObjectMapRemovalMutators<T>, ILongToObjectMapMutators<T>, ILongToObjectMapGetters<T> {
}
