package dev.jdata.db.schema.model.schemaobjects;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMap;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapBuilder;

final class HeapSchemaObjects<T extends SchemaObject>

        extends SchemaObjects<

                        T,
                        IHeapIndexList<T>,
                        IHeapLongToObjectDynamicMap<T>,
                        IHeapLongToObjectDynamicMapBuilder<T>,
                        IHeapLongToObjectDynamicMapAllocator<T>,
                        HeapSchemaObjects<T>>

        implements IHeapSchemaObjects<T> {

    private static final HeapSchemaObjects<?> emptySchemaObjects = new HeapSchemaObjects<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    static <T extends SchemaObject> HeapSchemaObjects<T> empty() {

        return (HeapSchemaObjects<T>)emptySchemaObjects;
    }

    HeapSchemaObjects(AllocationType allocationType, IHeapIndexList<T> schemaObjects, IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator,
            IntFunction<T[]> createValuesArray) {
        super(allocationType, schemaObjects, longToObjectMapAllocator, createValuesArray);
    }

    private HeapSchemaObjects(AllocationType allocationType) {
        super(allocationType);
    }
}
