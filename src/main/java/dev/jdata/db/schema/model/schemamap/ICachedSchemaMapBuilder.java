package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;

public interface ICachedSchemaMapBuilder<T extends SchemaObject> extends ISchemaMapBuilder<T, ICachedSchemaMap<T>, IHeapSchemaMap<T>> {

}
