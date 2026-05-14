package dev.jdata.db.schema.allocators.schemaobjects;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.DDLObjectTypeAllocators;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilderAllocator;

abstract class SchemaObjectsBuilderAllocators<T extends ISchemaObjectsBuilderAllocator<?, ?, ?>> extends DDLObjectTypeAllocators<T> implements ISchemaObjectsBuilderAllocators {

    SchemaObjectsBuilderAllocators(IntFunction<T[]> createSchemaObjectsBuilderAllocatorArray, Function<DDLObjectType, T> createSchemaObjectsBuilderAllocator) {
        super(createSchemaObjectsBuilderAllocatorArray, (t, g) -> createSchemaObjectsBuilderAllocator.apply(t));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected final Class<T> getCachedObjectClass() {

        return (Class<T>)(Class<?>)ISchemaObjectsBuilderAllocator.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <U extends SchemaObject, V extends ISchemaObjects<U>, W extends ISchemaObjectsBuilder<U, V, ?>> ISchemaObjectsBuilderAllocator<U, V, W> getAllocator(
            DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (ISchemaObjectsBuilderAllocator<U, V, W>)getInstance(ddlObjectType);
    }
}
