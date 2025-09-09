package dev.jdata.db.schema.storage;

import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;

public interface IDatabaseSchemaSerialization<T extends IAllCompleteSchemaMaps> extends IDatabaseSchemaSerializer, IDatabaseSchemaDeserializer<T> {

}
