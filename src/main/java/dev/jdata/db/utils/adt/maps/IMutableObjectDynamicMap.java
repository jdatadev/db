package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

interface IMutableObjectDynamicMap<K, V> extends IObjectContainsKeyMap<K>, IClearable, IObjectKeyDynamicMapRemovalMutators<K>,
        IObjectToObjectDynamicMapRemovalMutators<K, V>, IObjectToObjectCommonMapMutators<K, V>, IObjectToObjectDynamicMapCommon<K, V> {

}