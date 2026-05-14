package dev.jdata.db.schema.model.diff.schemamap;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapSimpleDiffSchemaMapBuilderAllocator<T extends SchemaObject>

        extends SimpleDiffSchemaMapBuilderAllocator<T, IHeapSchemaObjects<T>, IHeapDiffSchemaMap, IHeapDiffSchemaMap, IHeapDiffSchemaMapBuilder<T>> {

    static final HeapSimpleDiffSchemaMapBuilderAllocator<?> INSTANCE = new HeapSimpleDiffSchemaMapBuilderAllocator<>();

    private HeapSimpleDiffSchemaMapBuilderAllocator() {

    }

    @Override
    public IHeapDiffSchemaMapBuilder<T> createBuilder() {

        return new HeapSimpleDiffSchemaMapBuilder<T>(AllocationType.HEAP_ALLOCATOR);
    }

    @Override
    public void freeBuilder(IHeapDiffSchemaMapBuilder<T> builder) {

        Objects.requireNonNull(builder);
    }

    @Override
    public void freeImmutable(IHeapDiffSchemaMap immutable) {

        Objects.requireNonNull(immutable);
    }
}
