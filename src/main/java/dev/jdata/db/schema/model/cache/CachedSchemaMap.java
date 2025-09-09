package dev.jdata.db.schema.model.cache;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.CachedIndexList;
import dev.jdata.db.utils.allocators.ICacheable;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;

public final class CachedSchemaMap<T extends SchemaObject> extends SchemaMap<T, CachedIndexList<T>, CachedSchemaMap<T>> implements ICacheable {

    private static final CachedSchemaMap<?> emptySchemaMap = new CachedSchemaMap<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    public static <T extends SchemaObject> CachedSchemaMap<T> empty() {

        return (CachedSchemaMap<T>)emptySchemaMap;
    }

    CachedSchemaMap(AllocationType allocationType) {
        super(allocationType);
    }

    CachedSchemaMap(AllocationType allocationType, CachedIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }
}
