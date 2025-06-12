package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

interface IMutableLongToObjectDynamicMap<T>

        extends ILongToObjectDynamicMapCommon<T>, IClearable, ILongKeyDynamicMapRemovalMutators, ILongToObjectDynamicMapRemovalMutators<T>, ILongToObjectCommonMapMutators<T> {
}
