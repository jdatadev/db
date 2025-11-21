package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapSchemaMap<T extends SchemaObject> extends ISchemaMap<T>, IHeapContainsMarker {

    public static <T extends SchemaObject> IHeapSchemaMap<T> empty() {

        return HeapSchemaMap.empty();
    }

    public static <T extends SchemaObject> IHeapSchemaMap<T> of(IHeapIndexList<T> schemaObjects, IntFunction<T[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(longToObjectMapAllocator);

        return new HeapSchemaMap<>(AllocationType.HEAP, schemaObjects, createValuesArray, longToObjectMapAllocator);
    }
}
