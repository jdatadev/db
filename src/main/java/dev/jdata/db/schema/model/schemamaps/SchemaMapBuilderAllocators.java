package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.DDLObjectTypeAllocators;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;

abstract class SchemaMapBuilderAllocators<T extends ISchemaMapBuilderAllocator<?, ?, ?>> extends DDLObjectTypeAllocators<T> implements ISchemaMapBuilderAllocators {

    SchemaMapBuilderAllocators(IntFunction<T[]> createSchemaMapBuilderAllocatorArray, Function<DDLObjectType, T> createSchemaMapBuilderAllocator) {
        super(createSchemaMapBuilderAllocatorArray, (t, g) -> createSchemaMapBuilderAllocator.apply(t));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final Class<T> getCachedObjectClass() {

        return (Class<T>)(Class<?>)ISchemaMapBuilderAllocator.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <U extends SchemaObject, V extends ISchemaMap<U>, W extends ISchemaMapBuilder<U, V, ?>> ISchemaMapBuilderAllocator<U, V, W>
    getAllocator(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (ISchemaMapBuilderAllocator<U, V, W>)getInstance(ddlObjectType);
    }
}
