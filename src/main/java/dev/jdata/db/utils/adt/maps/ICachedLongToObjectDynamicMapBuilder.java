package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.builders.ICachedContainsBuilderMarker;

public interface ICachedLongToObjectDynamicMapBuilder<V>

        extends ILongToObjectDynamicMapBuilder<V, ICachedLongToObjectDynamicMap<V>, IHeapLongToObjectDynamicMap<V>>, ICachedContainsBuilderMarker {

}
