package dev.jdata.db.storage.backend;

import java.util.Objects;

import dev.jdata.db.schema.types.SchemaCustomType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.storage.backend.tabledata.NumStorageBitsGetter;

public final class ParameterizedNumStorageBitsGetter implements NumStorageBitsGetter {

    private final NumStorageBitsParameters parameters;
    private final BaseMinNumStorageBitsAdapter minNumBitsVisitor;
    private final BaseMaxNumStorageBitsAdapter maxNumBitsVisitor;

    public ParameterizedNumStorageBitsGetter(NumStorageBitsParameters parameters) {

        this.parameters = Objects.requireNonNull(parameters);

        this.minNumBitsVisitor = new BaseMinNumStorageBitsAdapter() {

            @Override
            public Integer onCustomType(SchemaCustomType schemaDataType, NumStorageBitsParameters parameter) {

                throw new UnsupportedOperationException();
            }
        };

        this.maxNumBitsVisitor = new BaseMaxNumStorageBitsAdapter() {

            @Override
            public final Integer onCustomType(SchemaCustomType schemaDataType, NumStorageBitsParameters parameter) {

                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int getMinNumBits(SchemaDataType schemaDataType) {

        return schemaDataType.visit(minNumBitsVisitor, parameters);
    }

    @Override
    public int getMaxNumBits(SchemaDataType schemaDataType) {

        return schemaDataType.visit(maxNumBitsVisitor, parameters);
    }
}
