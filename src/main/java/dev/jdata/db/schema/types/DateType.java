package dev.jdata.db.schema.types;

public final class DateType extends TimeBasedType {

    public static final DateType NULLABLE = new DateType(true);
    public static final DateType NON_NULLABLE = new DateType(false);

    public static DateType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private DateType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onDateType(this, parameter);
    }
}
