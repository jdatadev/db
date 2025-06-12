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

    public final boolean equals(StringResolver thisStringResolver, Column other, StringResolver otherStringResolver, boolean caseSensitive) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        if (this == other) {

            result = true;
        }
        else if (!super.equals(thisStringResolver, other, otherStringResolver, caseSensitive)) {

            result = false;
        }
        else if (getClass() != other.getClass()) {

            result = false;
        }
        else {
            result = equalsInstanceVariables(other);
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (!super.equals(object)) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final Column other = (Column)object;

            result = equalsInstanceVariables(other);
        }

        return result;
    }

    private boolean equalsInstanceVariables(Column other) {

        return schemaType.equals(other.schemaType) && nullable == other.nullable && Objects.equals(checkCondition, other.checkCondition);
    }

    @Override
    public void toString(StringResolver stringResolver, StringBuilder sb) {

        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sb);

        sb.append(getClass().getSimpleName());

        sb.append(" [super=");
        super.toString(stringResolver, sb);

        sb.append(", schemaType=").append(schemaType).append(", nullable=").append(nullable).append(", checkCondition=").append(checkCondition).append(']');
    }
}
