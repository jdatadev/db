package dev.jdata.db.schema.model.schemaobjects;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.lists.ICachedIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedIndexListBuilder;
import dev.jdata.db.utils.adt.lists.ICachedMutableIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.maps.ICachedLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;

@Deprecated // check parameters for create() and heapCreate() ?
final class CachedSchemaObjectsBuilder<T extends SchemaObject>

        extends SchemaObjectsBuilder<

                        T,
                        ICachedIndexList<T>,
                        ICachedMutableIndexList<T>,
                        IHeapIndexList<T>,
                        ICachedIndexListBuilder<T>,
                        ICachedIndexListAllocator<T>,
                        ICachedSchemaObjects<T>,
                        IHeapSchemaObjects<T>>

        implements ICachedSchemaObjectsBuilder<T> {

    private final ICachedLongToObjectDynamicMapAllocator<T> cachedLongToObjectMapAllocator;
    private final IHeapLongToObjectDynamicMapAllocator<T> heapLongToObjectMapAllocator;

    CachedSchemaObjectsBuilder(AllocationType allocationType, ICachedIndexListAllocator<T> indexListAllocator,
            ICachedLongToObjectDynamicMapAllocator<T> cachedLongToObjectMapAllocator, IHeapLongToObjectDynamicMapAllocator<T> heapLongToObjectMapAllocator,
            IntFunction<T[]> createValuesArray) {
        super(allocationType, indexListAllocator, createValuesArray);

        this.cachedLongToObjectMapAllocator = Objects.requireNonNull(cachedLongToObjectMapAllocator);
        this.heapLongToObjectMapAllocator = Objects.requireNonNull(heapLongToObjectMapAllocator);
    }

    @Override
    protected ICachedSchemaObjects<T> empty() {

        return CachedSchemaObjects.empty();
    }

    @Override
    protected IHeapSchemaObjects<T> heapEmpty() {

        return HeapSchemaObjects.empty();
    }

    @Override
    CachedSchemaObjects<T> create(ICachedIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(createValuesArray);

        return new CachedSchemaObjects<>(AllocationType.CACHING_ALLOCATOR, schemaObjects, cachedLongToObjectMapAllocator, createValuesArray);
    }

    @Override
    IHeapSchemaObjects<T> heapCreate(ICachedIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(createValuesArray);

        final IHeapIndexList<T> heapIndexList = IHeapIndexList.copyOf(schemaObjects, createValuesArray);

        return new HeapSchemaObjects<>(AllocationType.HEAP_ALLOCATOR, heapIndexList, heapLongToObjectMapAllocator, createValuesArray);
    }
}
