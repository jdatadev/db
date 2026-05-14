package dev.jdata.db.schema.allocators.schemaobjects;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilderAllocator;

public interface ISchemaObjectsBuilderAllocators {

    <T extends SchemaObject, U extends ISchemaObjects<T>, V extends ISchemaObjectsBuilder<T, U, ?>> ISchemaObjectsBuilderAllocator<T, U, V> getAllocator(
            DDLObjectType ddlObjectType);
}
