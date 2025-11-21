package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.model.databaseschema.ICompleteDatabaseSchema;

public interface IEffectiveDatabaseSchema extends ICompleteDatabaseSchema {

    @Override
    DatabaseId getDatabaseId();
}
