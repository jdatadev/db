package dev.jdata.db.schema.model.schemaobjects;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.ICachedIndexList;
import dev.jdata.db.utils.adt.maps.ICachedLongToObjectDynamicMap;
import dev.jdata.db.utils.adt.maps.ICachedLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.adt.maps.ICachedLongToObjectDynamicMapBuilder;

final class CachedSchemaObjects<T extends SchemaObject>

        extends SchemaObjects<

                        T,
                        ICachedIndexList<T>,
                        ICachedLongToObjectDynamicMap<T>,
                        ICachedLongToObjectDynamicMapBuilder<T>,
                        ICachedLongToObjectDynamicMapAllocator<T>,
                        CachedSchemaObjects<T>>

        implements ICachedSchemaObjects<T> {

    private static final CachedSchemaObjects<?> emptySchemaObjects = new CachedSchemaObjects<>(AllocationType.HEAP_CONSTANT);

    @SuppressWarnings("unchecked")
    static <T extends SchemaObject> CachedSchemaObjects<T> empty() {

        return (CachedSchemaObjects<T>)emptySchemaObjects;
    }

    CachedSchemaObjects(AllocationType allocationType, ICachedIndexList<T> schemaObjects, ICachedLongToObjectDynamicMapAllocator<T> longToObjectMapAllocator,
            IntFunction<T[]> createValuesArray) {
        super(allocationType, schemaObjects, longToObjectMapAllocator, createValuesArray);
    }

    private CachedSchemaObjects(AllocationType allocationType) {
        super(allocationType);
    }
}
