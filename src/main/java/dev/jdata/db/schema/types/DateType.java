package dev.jdata.db.schema.types;

public final class DateType extends TimeBasedType {

    public static final DateType INSTANCE = new DateType();

    private DateType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onDateType(this, parameter);
    }
}
