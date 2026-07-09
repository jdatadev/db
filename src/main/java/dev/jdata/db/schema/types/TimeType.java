package dev.jdata.db.schema.types;

public final class TimeType extends TimeBasedType {

    public static final TimeType INSTANCE = new TimeType();

    private TimeType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onTimeType(this, parameter);
    }
}
