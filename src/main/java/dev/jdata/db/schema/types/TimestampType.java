package dev.jdata.db.schema.types;

public final class TimestampType extends TimeBasedType {

    public static final TimestampType INSTANCE = new TimestampType();

    private TimestampType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onTimestampType(this, parameter);
    }
}
