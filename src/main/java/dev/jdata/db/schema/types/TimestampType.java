package dev.jdata.db.schema.types;

public final class TimestampType extends TimeBasedType {

    public static final TimestampType NULLABLE = new TimestampType(true);
    public static final TimestampType NON_NULLABLE = new TimestampType(false);

    public static TimestampType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private TimestampType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onTimestampType(this, parameter);
    }
}
