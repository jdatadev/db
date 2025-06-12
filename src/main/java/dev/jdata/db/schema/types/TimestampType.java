package dev.jdata.db.schema.types;

public final class TimestampType extends TimeBasedType {

    public static final TimestampType INSTANCE = new TimestampType();
/*
    public static final TimestampType NULLABLE = new TimestampType(true);
    public static final TimestampType NON_NULLABLE = new TimestampType(false);

    public static TimestampType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private TimestampType(boolean nullable) {
        super(nullable);
    }
*/

    private TimestampType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onTimestampType(this, parameter);
    }
}
