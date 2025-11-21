package dev.jdata.db.utils.adt.maps;

public interface IMutableLongToObjectWithRemoveStaticMap<V>

        extends IMutableLongToObjectBaseStaticMap<V>, ILongKeyStaticMapRemovalMutators, ILongToObjectStaticMapRemovalMutators<V> {
}
