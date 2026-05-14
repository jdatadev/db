package dev.jdata.db.schema.allocators.schemaobjects;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjectsBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;

public final class HeapSchemaObjectsBuilderAllocators extends SchemaObjectsBuilderAllocators<IHeapSchemaObjectsBuilderAllocator<?>> {

    public static final HeapSchemaObjectsBuilderAllocators INSTANCE = new HeapSchemaObjectsBuilderAllocators();

    private HeapSchemaObjectsBuilderAllocators() {
        super(IHeapSchemaObjectsBuilderAllocator[]::new, t -> createSchemaMapBuilderAllocator(t.getCreateArray()));
    }

    private static <T extends SchemaObject> IHeapSchemaObjectsBuilderAllocator<T> createSchemaMapBuilderAllocator(IntFunction<T[]> createArray) {

        final IHeapIndexListAllocator<T> indexListAllocator = IHeapIndexListAllocator.create(createArray);
        final IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator = IHeapLongToObjectDynamicMapAllocator.create(createArray);

        return IHeapSchemaObjectsBuilderAllocator.create(indexListAllocator, longToObjectMapAllocator, createArray);
    }
}
