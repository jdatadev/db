package dev.jdata.db.schema.storage;

import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;

public interface IDatabaseSchemaSerializer<E extends Exception> {

    void serialize(IEffectiveDatabaseSchema databaseSchema) throws E;
}
