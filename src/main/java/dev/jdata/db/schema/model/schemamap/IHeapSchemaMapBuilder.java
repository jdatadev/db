package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapSchemaMapBuilder<T extends SchemaObject> extends ISchemaMapBuilder<T, IHeapSchemaMap<T>, IHeapSchemaMap<T>> {

    public static <T extends SchemaObject> IHeapSchemaMapBuilder<T> create(AllocationType allocationType, IntFunction<T[]> createValuesArray,
            IHeapIndexListAllocator<T> indexListAllocator, IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator, int initialCapacity) {

        return new HeapSchemaMapBuilder<>(allocationType, createValuesArray, indexListAllocator, longToObjectMapAllocator, initialCapacity);
    }
}
