package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.ISchemaObjects;
import dev.jdata.db.schema.model.objects.SchemaObject;

public interface ISchemaMapBuilders<T extends SchemaObject, U> {

    U addSchemaObject(T schemaObject);
    U addSchemaObjects(ISchemaObjects schemaObjects);
}
