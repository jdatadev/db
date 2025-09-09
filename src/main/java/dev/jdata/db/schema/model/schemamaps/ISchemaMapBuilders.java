package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.ISchemaObjects;
import dev.jdata.db.schema.model.objects.SchemaObject;

public interface ISchemaMapBuilders<SCHEMA_OBJECT extends SchemaObject> {

    void addSchemaObject(SCHEMA_OBJECT schemaObject);
    void addSchemaObjects(ISchemaObjects schemaObjects);
}
