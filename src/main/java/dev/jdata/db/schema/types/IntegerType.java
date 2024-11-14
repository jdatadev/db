package dev.jdata.db.schema.types;

public final class IntegerType extends BaseIntegerType {

    public static final IntegerType NULLABLE = new IntegerType(true);
    public static final IntegerType NON_NULLABLE = new IntegerType(false);

    public static IntegerType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private IntegerType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onIntegerType(this, parameter);
    }
}
