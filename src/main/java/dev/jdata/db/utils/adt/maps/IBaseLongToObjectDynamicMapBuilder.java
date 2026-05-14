package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;

interface IBaseLongToObjectDynamicMapBuilder<V, T extends IBaseLongToObjectDynamicMap<V>, U extends IBaseLongToObjectDynamicMap<V> & IHeapContainsMarker>

        extends IMapBuilder<ILongAnyOrderAddable, T, U> {

    @Deprecated // optimize away containsKey?
    void add(long key, V value);
}
