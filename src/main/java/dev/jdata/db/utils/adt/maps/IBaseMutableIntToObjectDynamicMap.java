package dev.jdata.db.utils.adt.maps;

public interface IBaseMutableIntToObjectDynamicMap<V>

        extends IMutableIntToObjectMap<V>, IIntToObjectDynamicMapCommon<V>, IIntKeyDynamicMapRemovalMutators, IIntToObjectDynamicMapRemovalMutators<V> {
}
