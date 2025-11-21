package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;

final class CachedSchemaMap<T extends SchemaObject> extends SchemaMap<T, ICachedIndexList<T>, CachedSchemaMap<T>> implements ICachedSchemaMap<T> {

    private static final CachedSchemaMap<?> emptySchemaMap = new CachedSchemaMap<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    static <T extends SchemaObject> CachedSchemaMap<T> empty() {

        return (CachedSchemaMap<T>)emptySchemaMap;
    }

    CachedSchemaMap(AllocationType allocationType) {
        super(allocationType);
    }

    CachedSchemaMap(AllocationType allocationType, ICachedIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }
}
