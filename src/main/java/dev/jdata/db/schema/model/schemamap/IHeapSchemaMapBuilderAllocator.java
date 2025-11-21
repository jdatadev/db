package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;

public interface IHeapSchemaMapBuilderAllocator<T extends SchemaObject> extends ISchemaMapBuilderAllocator<T, IHeapSchemaMap<T>, IHeapSchemaMapBuilder<T>> {

    public static <T extends SchemaObject> IHeapSchemaMapBuilderAllocator<T> create(IntFunction<T[]> createValuesArray, IHeapIndexListAllocator<T> indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {

        return new HeapSchemaMapBuilderAllocator<>(createValuesArray, indexListAllocator, longToObjectMapAllocator);
    }
}
