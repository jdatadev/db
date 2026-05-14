package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.ISchemaObjectsByObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;

@Deprecated // in use?
public interface ISchemaMapBuilders<T extends SchemaObject, U> {

    U addSchemaObject(T schemaObject);
    U addSchemaObjects(ISchemaObjectsByObjectType schemaObjects);
}
