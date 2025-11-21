package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.adt.lists.ICachedMutableIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;

final class CachedSchemaMapBuilder<T extends SchemaObject>

        extends SchemaMapBuilder<

                        T,
                        ICachedIndexList<T>,
                        ICachedMutableIndexList<T>,
                        IHeapIndexList<T>,
                        ICachedIndexListBuilder<T>,
                        ICachedIndexListAllocator<T>,
                        ICachedSchemaMap<T>,
                        IHeapSchemaMap<T>>

        implements ICachedSchemaMapBuilder<T> {

    CachedSchemaMapBuilder(AllocationType allocationType, IntFunction<T[]> createValuesArray, ICachedIndexListAllocator<T> indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {
        super(allocationType, createValuesArray, indexListAllocator, longToObjectMapAllocator);
    }

    @Override
    protected ICachedSchemaMap<T> empty() {

        return CachedSchemaMap.empty();
    }

    @Override
    protected IHeapSchemaMap<T> heapEmpty() {

        return HeapSchemaMap.empty();
    }

    @Override
    CachedSchemaMap<T> create(ICachedIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray, IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        return new CachedSchemaMap<>(AllocationType.HEAP_ALLOCATOR, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }

    @Override
    IHeapSchemaMap<T> heapCreate(ICachedIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray, IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        final IHeapIndexList<T> heapIndexList = IHeapIndexList.copyOf(createValuesArray, schemaObjects);

        return new HeapSchemaMap<>(AllocationType.HEAP_ALLOCATOR, heapIndexList, createValuesArray, longToObjectMapAllocator);
    }
}
