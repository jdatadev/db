package dev.jdata.db.schema.types;

public final class DateType extends TimeBasedType {

    public static final DateType INSTANCE = new DateType();

    private DateType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onDateType(this, parameter);
    }
}
