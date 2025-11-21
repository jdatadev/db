package dev.jdata.db.utils.adt.maps;

public interface IMutableIntToObjectDynamicMap<V>

        extends IMutableIntToObjectMap<V>, IIntToObjectDynamicMapCommon<V>, IIntKeyDynamicMapRemovalMutators, IIntToObjectDynamicMapRemovalMutators<V> {
}