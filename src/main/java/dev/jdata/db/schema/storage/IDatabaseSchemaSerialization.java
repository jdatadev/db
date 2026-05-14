package dev.jdata.db.schema.storage;

import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMap;

public interface IDatabaseSchemaSerialization<T extends ICompleteSchemaMap> extends IDatabaseSchemaSerializer, IDatabaseSchemaDeserializer<T> {

}
