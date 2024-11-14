package dev.jdata.db.storage.backend;

import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.schema.types.SchemaDataTypeVisitor;

abstract class BaseNumStorageBitsAdapter implements SchemaDataTypeVisitor<NumStorageBitsParameters, Integer> {

    final int getNumStorageBits(SchemaDataType schemaDataType, NumStorageBitsParameters parameters) {

        return schemaDataType.visit(this, parameters);
    }
}
