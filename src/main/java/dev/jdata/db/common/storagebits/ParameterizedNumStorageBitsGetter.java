package dev.jdata.db.common.storagebits;

import java.util.Objects;

import dev.jdata.db.schema.types.SchemaCustomType;
import dev.jdata.db.schema.types.SchemaDataType;

public final class ParameterizedNumStorageBitsGetter implements INumStorageBitsGetter {

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
