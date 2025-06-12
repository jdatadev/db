package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

interface IMutableCommonMap<K, V, M extends IBaseObjectToObjectMapCommon<K, V, M>>

        extends IBaseObjectToObjectMapCommon<K, V, M>, IClearable, IKeyMap<K[]>, IObjectToObjectCommonMapMutators<K, V> {
}
