package dev.jdata.db.ddl.model.diff;

import dev.jdata.db.schema.model.objects.DBNamedIdentifiableObject;

public abstract class SchemaObjectDiff extends DBNamedIdentifiableObject {

    SchemaObjectDiff(long parsedName, long hashName, int id) {
        super(parsedName, hashName, id);
    }
}
