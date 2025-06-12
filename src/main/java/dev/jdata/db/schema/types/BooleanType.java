package dev.jdata.db.schema.types;

public final class BooleanType extends ScalarType {

    public static final BooleanType INSTANCE = new BooleanType();
/*
    public static final BooleanType NULLABLE = new BooleanType(true);
    public static final BooleanType NON_NULLABLE = new BooleanType(false);

    public static BooleanType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private BooleanType(boolean nullable) {
        super(nullable);
    }
*/

    private BooleanType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onBooleanType(this, parameter);
    }
}
