package dev.jdata.db.schema.model.schemaobjects;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexList;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;

final class HeapSchemaObjectsBuilder<T extends SchemaObject>

        extends SchemaObjectsBuilder<

                        T,
                        IHeapIndexList<T>,
                        IHeapMutableIndexList<T>,
                        IHeapIndexList<T>,
                        IHeapIndexListBuilder<T>,
                        IHeapIndexListAllocator<T>,
                        IHeapSchemaObjects<T>,
                        IHeapSchemaObjects<T>>

        implements IHeapSchemaObjectsBuilder<T> {

    private final IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator;

    HeapSchemaObjectsBuilder(AllocationType allocationType, int initialCapacity, IHeapIndexListAllocator<T> indexListAllocator,
            IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator, IntFunction<T[]> createValuesArray) {
        super(allocationType, initialCapacity, indexListAllocator, createValuesArray);

        this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
    }

    @Override
    protected HeapSchemaObjects<T> empty() {

        return HeapSchemaObjects.empty();
    }

    @Override
    protected IHeapSchemaObjects<T> heapEmpty() {

        return empty();
    }

    @Override
    IHeapSchemaObjects<T> create(IHeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray) {

        return new HeapSchemaObjects<>(AllocationType.HEAP_ALLOCATOR, schemaObjects, longToObjectMapAllocator, createValuesArray);
    }

    @Override
    IHeapSchemaObjects<T> heapCreate(IHeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray) {

        return create(schemaObjects, createValuesArray);
    }
}
