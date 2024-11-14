package dev.jdata.db.storage.backend.tabledata;

import dev.jdata.db.schema.types.SchemaDataType;

public interface NumStorageBitsGetter {

    int getMinNumBits(SchemaDataType schemaDataType);
    int getMaxNumBits(SchemaDataType schemaDataType);
}

