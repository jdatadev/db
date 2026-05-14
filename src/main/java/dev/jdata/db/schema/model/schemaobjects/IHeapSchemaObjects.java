package dev.jdata.db.schema.model.schemaobjects;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.maps.IHeapLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapSchemaObjects<T extends SchemaObject> extends ISchemaObjects<T>, IHeapContainsMarker {

    public static <T extends SchemaObject> IHeapSchemaObjects<T> empty() {

        return HeapSchemaObjects.empty();
    }

    @Deprecated // move checks?
    public static <T extends SchemaObject> IHeapSchemaObjects<T> of(IHeapIndexList<T> schemaObjects, IHeapLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator,
            IntFunction<T[]> createValuesArray) {

        Objects.requireNonNull(schemaObjects);
        Objects.requireNonNull(longToObjectMapAllocator);
        Objects.requireNonNull(createValuesArray);

        return new HeapSchemaObjects<>(AllocationType.HEAP, schemaObjects, longToObjectMapAllocator, createValuesArray);
    }
}
