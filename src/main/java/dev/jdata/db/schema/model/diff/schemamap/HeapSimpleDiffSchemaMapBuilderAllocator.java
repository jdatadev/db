package dev.jdata.db.schema.model.diff.schemamap;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

final class HeapSimpleDiffSchemaMapBuilderAllocator<T extends SchemaObject>

        extends SimpleDiffSchemaMapBuilderAllocator<T, IHeapSchemaObjects<T>, IHeapDiffSchemaMap, IHeapDiffSchemaMap, IHeapDiffSchemaMapBuilder<T>> {

    static <T extends SchemaObject> HeapSimpleDiffSchemaMapBuilderAllocator<T> create(IntFunction<T[]> createSchemaObjectArray) {

        Objects.requireNonNull(createSchemaObjectArray);

        return new HeapSimpleDiffSchemaMapBuilderAllocator<>(createSchemaObjectArray);
    }

    private final IntFunction<T[]> createSchemaObjectArray;

    private HeapSimpleDiffSchemaMapBuilderAllocator(IntFunction<T[]> createSchemaObjectArray) {

        this.createSchemaObjectArray = Objects.requireNonNull(createSchemaObjectArray);
    }

    @Override
    public IHeapDiffSchemaMapBuilder<T> createBuilder() {

        return new HeapSimpleDiffSchemaMapBuilder<T>(AllocationType.HEAP_ALLOCATOR, createSchemaObjectArray);
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
