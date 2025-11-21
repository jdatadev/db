package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;

public interface ISchemaMapBuilderAllocators {

    <T extends SchemaObject, U extends ISchemaMap<T>, V extends ISchemaMapBuilder<T, U, ?>> ISchemaMapBuilderAllocator<T, U, V> getAllocator(DDLObjectType ddlObjectType);
}
