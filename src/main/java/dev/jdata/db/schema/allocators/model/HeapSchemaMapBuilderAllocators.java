package dev.jdata.db.schema.allocators.model;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.HeapSchemaMap.HeapSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.allocators.ILongToObjectMaxDistanceMapAllocator;
import dev.jdata.db.utils.allocators.LongToObjectMaxDistanceMapAllocator;

public final class HeapSchemaMapBuilderAllocators extends SchemaMapBuilderAllocators {

    public static final HeapSchemaMapBuilderAllocators INSTANCE = new HeapSchemaMapBuilderAllocators();

    private HeapSchemaMapBuilderAllocators() {
        super(t -> createSchemaMapBuilderAllocator(t.getCreateArray()));
    }

    private static <T extends SchemaObject> HeapSchemaMapBuilderAllocator<T> createSchemaMapBuilderAllocator(IntFunction<T[]> createArray) {

        final HeapIndexListAllocator<T> indexListAllocator = new HeapIndexListAllocator<>(createArray);
        final ILongToObjectMaxDistanceMapAllocator<T> longToObjectMapAllocator = new LongToObjectMaxDistanceMapAllocator<>(createArray);

        return new HeapSchemaMapBuilderAllocator<>(createArray, indexListAllocator, longToObjectMapAllocator);
    }
}
