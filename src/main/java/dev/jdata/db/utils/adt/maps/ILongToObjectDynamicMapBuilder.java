package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface ILongToObjectDynamicMapBuilder<V, T extends ILongToObjectDynamicMap<V>, U extends ILongToObjectDynamicMap<V> & IHeapContainsMarker>

        extends IBaseLongToObjectDynamicMapBuilder<V, T, U> {

}
