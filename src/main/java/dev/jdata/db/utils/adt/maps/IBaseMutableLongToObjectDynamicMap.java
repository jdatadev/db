package dev.jdata.db.utils.adt.maps;

interface IBaseMutableLongToObjectDynamicMap<V>

        extends IMutableLongToObjectMap<V>, ILongToObjectDynamicMapCommon<V>, ILongKeyDynamicMapRemovalMutators, ILongToObjectDynamicMapRemovalMutators<V> {
}
