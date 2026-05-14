package dev.jdata.db.schema.model.schemaobjects;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapSchemaObjectsBuilderAllocator<T extends SchemaObject>

    extends SchemaObjectsBuilderAllocator<T, IHeapIndexList<T>, IHeapIndexListBuilder<T>, IHeapIndexListAllocator<T>, IHeapSchemaObjects<T>, IHeapSchemaObjectsBuilder<T>>
    implements IHeapSchemaObjectsBuilderAllocator<T> {

    private final IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator;

    HeapSchemaObjectsBuilderAllocator(IHeapIndexListAllocator<T> indexListAllocator, IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator,
            IntFunction<T[]> createValuesArray) {
        super(indexListAllocator, createValuesArray);

        this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
    }

    @Override
    public IHeapSchemaObjectsBuilder<T> createBuilder(int minimumCapacity) {

        checkCreateBuilderParameters(minimumCapacity);

        return IHeapSchemaObjectsBuilder.create(AllocationType.HEAP_ALLOCATOR, minimumCapacity, getIndexListAllocator(), longToObjectMapAllocator, getCreateValuesArray());
    }

    @Override
    public void freeBuilder(IHeapSchemaObjectsBuilder<T> builder) {

        checkFreeCreatedBuilderParameters(builder);
    }
}
