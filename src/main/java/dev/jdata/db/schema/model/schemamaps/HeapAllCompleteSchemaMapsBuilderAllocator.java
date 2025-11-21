package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class HeapAllCompleteSchemaMapsBuilderAllocator

        extends AllCompleteSchemaMapsBuilderAllocator<IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMapsBuilder>
        implements IHeapAllCompleteSchemaMapsBuilderAllocator {

    public static final HeapAllCompleteSchemaMapsBuilderAllocator INSTANCE = new HeapAllCompleteSchemaMapsBuilderAllocator();

    @Override
    public IHeapAllCompleteSchemaMapsBuilder createBuilder() {

        return new HeapAllSimpleCompleteSchemaMapsBuilder(AllocationType.HEAP_ALLOCATOR);
    }

    @Override
    public void freeBuilder(IHeapAllCompleteSchemaMapsBuilder builder) {

        Objects.requireNonNull(builder);
    }

    @Override
    public void freeImmutable(IHeapAllCompleteSchemaMaps immutable) {

        Objects.requireNonNull(immutable);
    }
}
