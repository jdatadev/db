package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapSimpleCompleteSchemaMapBuilderAllocator

        extends SimpleCompleteSchemaMapBuilderAllocator<IHeapCompleteSchemaMap, IHeapCompleteSchemaMap, IHeapCompleteSchemaMapBuilder>
        implements IHeapCompleteSchemaMapBuilderAllocator {

    static final HeapSimpleCompleteSchemaMapBuilderAllocator INSTANCE = new HeapSimpleCompleteSchemaMapBuilderAllocator();

    @Override
    public IHeapCompleteSchemaMapBuilder createBuilder() {

        return new HeapSimpleCompleteSchemaMapBuilder(AllocationType.HEAP_ALLOCATOR);
    }

    @Override
    public void freeBuilder(IHeapCompleteSchemaMapBuilder builder) {

        checkFreeCreatedBuilderParameters(builder);

        Objects.requireNonNull(builder);
    }

    @Override
    public void freeImmutable(IHeapCompleteSchemaMap immutable) {

        checkFreeImmutableParameters(immutable);
    }
}
