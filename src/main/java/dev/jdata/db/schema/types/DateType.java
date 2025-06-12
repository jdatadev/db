package dev.jdata.db.schema.types;

public final class DateType extends TimeBasedType {

    public static final DateType INSTANCE = new DateType();
/*
    public static final DateType NULLABLE = new DateType(true);
    public static final DateType NON_NULLABLE = new DateType(false);

    public static DateType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private DateType(boolean nullable) {
        super(nullable);
    }
*/
    private DateType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onDateType(this, parameter);
    }
}
