package dev.jdata.db.ddl.model.diff;

import dev.jdata.db.schema.model.objects.DBNamedIdentifiableObject;
import dev.jdata.db.schema.model.objects.SchemaObject;

public abstract class SchemaObjectDiff extends DBNamedIdentifiableObject {

    SchemaObjectDiff(SchemaObject schemaObject) {
        super(schemaObject);
    }
}
