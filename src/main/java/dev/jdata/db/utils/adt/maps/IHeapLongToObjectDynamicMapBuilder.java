package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsBuilderMarker;

public interface IHeapLongToObjectDynamicMapBuilder<V>

        extends ILongToObjectDynamicMapBuilder<V, IHeapLongToObjectDynamicMap<V>, IHeapLongToObjectDynamicMap<V>>, IHeapContainsBuilderMarker {

}
