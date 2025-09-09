package dev.jdata.db.schema.model.cache;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.CachedIndexList;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CachedIndexListBuilder;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;

public final class CachedSchemaMapBuilder<T extends SchemaObject>

        extends SchemaMapBuilder<T, CachedIndexList<T>, CachedIndexListBuilder<T>, CacheIndexListAllocator<T>, CachedSchemaMap<T>, CachedSchemaMapBuilder<T>> {

    public CachedSchemaMapBuilder(IntFunction<T[]> createValuesArray, CacheIndexListAllocator<T> indexListAllocator,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {
        super(createValuesArray, indexListAllocator, longToObjectMapAllocator);
    }

    @Override
    protected CachedSchemaMap<T> empty() {

        return CachedSchemaMap.empty();
    }

    @Override
    protected CachedSchemaMap<T> create(CachedIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator) {

        return new CachedSchemaMap<>(AllocationType.HEAP_ALLOCATOR, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }
}