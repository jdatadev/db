package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapSchemaMapBuilderAllocator<T extends SchemaObject>

    extends SchemaMapBuilderAllocator<T, IHeapIndexList<T>, IHeapIndexListBuilder<T>, IHeapIndexListAllocator<T>, IHeapSchemaMap<T>, IHeapSchemaMapBuilder<T>>
    implements IHeapSchemaMapBuilderAllocator<T> {

    public HeapSchemaMapBuilderAllocator(IntFunction<T[]> createValuesArray, IHeapIndexListAllocator<T> indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {
        super(createValuesArray, indexListAllocator, longToObjectMapAllocator);
    }

    @Override
    public IHeapSchemaMapBuilder<T> createBuilder(int minimumCapacity) {

        return IHeapSchemaMapBuilder.create(AllocationType.HEAP_ALLOCATOR, getCreateValuesArray(), getIndexListAllocator(), getLongToObjectMapAllocator(), minimumCapacity);
    }

    @Override
    public void freeBuilder(IHeapSchemaMapBuilder<T> builder) {

        Objects.requireNonNull(builder);
    }
}
