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
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onBooleanType(this, parameter);
    }
}
