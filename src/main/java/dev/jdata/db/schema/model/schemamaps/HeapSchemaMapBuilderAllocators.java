package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.HeapSchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;

public final class HeapSchemaMapBuilderAllocators extends SchemaMapBuilderAllocators {

    public static final HeapSchemaMapBuilderAllocators INSTANCE = new HeapSchemaMapBuilderAllocators();

    private HeapSchemaMapBuilderAllocators() {
        super(t -> createSchemaMapBuilderAllocator(t.getCreateArray()));
    }

    private static <T extends SchemaObject> HeapSchemaMapBuilderAllocator<T> createSchemaMapBuilderAllocator(IntFunction<T[]> createArray) {

        final IHeapIndexListAllocator<T> indexListAllocator = IHeapIndexListAllocator.create(createArray);
        final IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator = IHeapMutableLongToObjectDynamicMapAllocator.create(createArray);

        return new HeapSchemaMapBuilderAllocator<>(createArray, indexListAllocator, longToObjectMapAllocator);
    }
}
