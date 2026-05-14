package dev.jdata.db.schema.model.schemaobjects;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.builders.IHeapContainsBuilderMarker;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapSchemaObjectsBuilder<T extends SchemaObject> extends ISchemaObjectsBuilder<T, IHeapSchemaObjects<T>, IHeapSchemaObjects<T>>, IHeapContainsBuilderMarker {

    public static <T extends SchemaObject> IHeapSchemaObjectsBuilder<T> create(AllocationType allocationType, int initialCapacity, IHeapIndexListAllocator<T> indexListAllocator,
            IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator, IntFunction<T[]> createValuesArray) {

        return new HeapSchemaObjectsBuilder<>(allocationType, initialCapacity, indexListAllocator, longToObjectMapAllocator, createValuesArray);
    }
}
