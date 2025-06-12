package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableIntToObjectDynamicMap<T>

        extends IIntToObjectDynamicMapCommon<T>, IClearable, IIntToObjectCommonMapMutators<T>, IIntKeyDynamicMapRemovalMutators, IIntToObjectDynamicMapRemovalMutators<T> {
}