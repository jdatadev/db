package dev.jdata.db.utils.adt.maps;

interface IMutableLongToObjectDynamicMap<V>

        extends IMutableLongToObjectMap<V>, ILongToObjectDynamicMapCommon<V>, ILongKeyDynamicMapRemovalMutators, ILongToObjectDynamicMapRemovalMutators<V> {
}
