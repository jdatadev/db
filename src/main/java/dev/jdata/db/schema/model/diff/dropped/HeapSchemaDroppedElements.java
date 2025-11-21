package dev.jdata.db.schema.model.diff.dropped;

import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IHeapMutableIntSet;

final class HeapSchemaDroppedElements

        extends SchemaDroppedElements<IHeapMutableIntSet, IMutableIntToObjectWithRemoveStaticMap<IHeapMutableIntSet>>
        implements IHeapSchemaDroppedElements {

    HeapSchemaDroppedElements(AllocationType allocationType) {
        super(allocationType);

        AllocationType.checkIsHeap(allocationType);
    }
}
