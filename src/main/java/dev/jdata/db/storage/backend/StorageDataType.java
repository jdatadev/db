package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.utils.checks.Checks;

@Deprecated
public final class StorageDataType {

    private final SchemaDataType schemaDataType;
    private final int minBits;
    private final int maxBits;

    public StorageDataType(SchemaDataType schemaDataType, int minBits, int maxBits) {

        this.schemaDataType = Objects.requireNonNull(schemaDataType);
        this.minBits = Checks.isNumBits(minBits);
        this.maxBits = Checks.isNumBits(maxBits);
    }

    public SchemaDataType getSchemaDataType() {
        return schemaDataType;
    }

    public int getMinBits() {
        return minBits;
    }

    public int getMaxBits() {
        return maxBits;
    }
}
