package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableIntToObjectMap<T> extends IIntContainsKeyMap, IClearable, IIntKeyMapRemovalMutators, IIntToObjectMapRemovalMutators<T>, IIntToObjectMapMutators<T>,
        IIntToObjectMapGetters<T> {

}