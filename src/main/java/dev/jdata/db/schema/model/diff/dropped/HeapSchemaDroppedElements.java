package dev.jdata.db.schema.model.diff.dropped;

import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;

public final class HeapSchemaDroppedElements<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends SchemaDroppedElements<T, U>
        implements IHeapSchemaDroppedElements {

    public HeapSchemaDroppedElements(AllocationType allocationType) {
        super(allocationType);

        AllocationType.checkIsHeap(allocationType);
    }
}
