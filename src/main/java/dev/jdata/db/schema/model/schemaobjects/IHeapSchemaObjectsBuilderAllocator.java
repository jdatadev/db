package dev.jdata.db.schema.model.schemaobjects;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;

public interface IHeapSchemaObjectsBuilderAllocator<T extends SchemaObject> extends ISchemaObjectsBuilderAllocator<T, IHeapSchemaObjects<T>, IHeapSchemaObjectsBuilder<T>> {

    public static <T extends SchemaObject> IHeapSchemaObjectsBuilderAllocator<T> create(IHeapIndexListAllocator<T> indexListAllocator,
            IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator, IntFunction<T[]> createValuesArray) {

        return new HeapSchemaObjectsBuilderAllocator<>(indexListAllocator, longToObjectMapAllocator, createValuesArray);
    }
}
