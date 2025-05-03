package dev.jdata.db.schema.model.objects;

import java.util.Objects;

import org.jutils.ast.objects.expression.Expression;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.types.SchemaDataType;

public final class Column extends DBNamedIdentifiableObject {

    private final SchemaDataType schemaType;
    private final boolean nullable;
    private final Expression checkCondition;

    public Column(long parsedName, long hashName, int id, SchemaDataType schemaType) {
        this(parsedName, hashName, id, schemaType, true);
    }

    public Column(long parsedName, long hashName, int id, SchemaDataType schemaType, boolean nullable) {
        this(parsedName, hashName, id, schemaType, nullable, null);
    }

    public Column(long parsedName, long hashName, int id, SchemaDataType schemaType, boolean nullable, Expression checkCondition) {
        super(parsedName, hashName, id);

        this.schemaType = Objects.requireNonNull(schemaType);
        this.nullable = nullable;
        this.checkCondition = checkCondition;
    }

    public SchemaDataType getSchemaType() {
        return schemaType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public Expression getCheckCondition() {
        return checkCondition;
    }

    public String toString(StringResolver stringResolver) {

        Objects.requireNonNull(stringResolver);

        final StringBuilder sb = new StringBuilder(100);

        sb.append(getClass().getSimpleName());

        sb.append(" [super=");

        toString(stringResolver, sb);

        sb.append(", schemaType=").append(schemaType).append(", nullable=").append(nullable).append(", checkCondition").append(checkCondition).append(']');

        return sb.toString();
    }
}
