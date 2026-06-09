package dev.jdata.db.schema.types;

public final class TimeType extends TimeBasedType {

    public static final TimeType INSTANCE = new TimeType();

    private TimeType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onTimeType(this, parameter);
    }
}
