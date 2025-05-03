package dev.jdata.db.common.storagebits;

import dev.jdata.db.schema.types.SchemaDataType;

public interface INumStorageBitsGetter {

    int getMinNumBits(SchemaDataType schemaDataType);
    int getMaxNumBits(SchemaDataType schemaDataType);
}

