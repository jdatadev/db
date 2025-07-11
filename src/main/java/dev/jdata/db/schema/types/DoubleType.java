package dev.jdata.db.schema.types;

public final class DoubleType extends BaseFloatingPointType {

    public static final DoubleType INSTANCE = new DoubleType();
/*
    public static final DoubleType NULLABLE = new DoubleType(true);
    public static final DoubleType NON_NULLABLE = new DoubleType(false);

    public static DoubleType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private DoubleType(boolean nullable) {
        super(nullable);
    }
*/

    private DoubleType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onDoubleType(this, parameter);
    }
}
