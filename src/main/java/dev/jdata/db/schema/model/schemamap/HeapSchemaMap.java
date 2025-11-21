package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;

final class HeapSchemaMap<T extends SchemaObject> extends SchemaMap<T, IHeapIndexList<T>, HeapSchemaMap<T>> implements IHeapSchemaMap<T> {

    private static final HeapSchemaMap<?> emptySchemaMap = new HeapSchemaMap<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    static <T extends SchemaObject> HeapSchemaMap<T> empty() {

        return (HeapSchemaMap<T>)emptySchemaMap;
    }

    HeapSchemaMap(AllocationType allocationType, IHeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }

    private HeapSchemaMap(AllocationType allocationType) {
        super(allocationType);
    }
}
