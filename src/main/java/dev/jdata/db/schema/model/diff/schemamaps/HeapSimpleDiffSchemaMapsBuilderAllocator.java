package dev.jdata.db.schema.model.diff.schemamaps;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapSimpleDiffSchemaMapsBuilderAllocator<T extends SchemaObject>

        extends SimpleDiffSchemaMapsBuilderAllocator<T, IHeapSchemaMap<T>, IHeapDiffSchemaMaps, IHeapDiffSchemaMaps, IHeapDiffSchemaMapsBuilder<T>> {

    static final HeapSimpleDiffSchemaMapsBuilderAllocator<?> INSTANCE = new HeapSimpleDiffSchemaMapsBuilderAllocator<>();

    private HeapSimpleDiffSchemaMapsBuilderAllocator() {

    }

    @Override
    public IHeapDiffSchemaMapsBuilder<T> createBuilder() {

        return new HeapDiffSchemaMapsBuilder<T>(AllocationType.HEAP_ALLOCATOR);
    }

    @Override
    public void freeBuilder(IHeapDiffSchemaMapsBuilder<T> builder) {

        Objects.requireNonNull(builder);
    }

    @Override
    public void freeImmutable(IHeapDiffSchemaMaps immutable) {

        Objects.requireNonNull(immutable);
    }
}
