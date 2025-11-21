package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;

final class HeapSchemaMapBuilder<T extends SchemaObject>

        extends SchemaMapBuilder<

                        T,
                        IHeapIndexList<T>,
                        IHeapMutableIndexList<T>,
                        IHeapIndexList<T>,
                        IHeapIndexListBuilder<T>,
                        IHeapIndexListAllocator<T>,
                        IHeapSchemaMap<T>,
                        IHeapSchemaMap<T>>

        implements IHeapSchemaMapBuilder<T> {

    HeapSchemaMapBuilder(AllocationType allocationType, IntFunction<T[]> createValuesArray, IHeapIndexListAllocator<T> indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator, int initialCapacity) {
        super(allocationType, createValuesArray, indexListAllocator, longToObjectMapAllocator, initialCapacity);
    }

    @Override
    protected HeapSchemaMap<T> empty() {

        return HeapSchemaMap.empty();
    }

    @Override
    protected IHeapSchemaMap<T> heapEmpty() {

        return empty();
    }

    @Override
    IHeapSchemaMap<T> create(IHeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray, IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {

        return new HeapSchemaMap<>(AllocationType.HEAP_ALLOCATOR, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }

    @Override
    IHeapSchemaMap<T> heapCreate(IHeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray, IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {

        return create(schemaObjects, createValuesArray, longToObjectMapAllocator);
    }
}
