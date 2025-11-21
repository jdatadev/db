package dev.jdata.db.schema.model.diff;

import dev.jdata.db.schema.model.databaseschema.IBaseDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.ISchemaDroppedElements;

public interface IDiffDatabaseSchema extends IBaseDatabaseSchema {

    ISchemaDroppedElements getDroppedSchemaObjects();
}
