package dev.jdata.db.schema.types;

public final class BigIntType extends BaseIntegerType {

    public static final BigIntType INSTANCE = new BigIntType();
/*
    public static final BigIntType NULLABLE = new BigIntType(true);
    public static final BigIntType NON_NULLABLE = new BigIntType(false);

    public static BigIntType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private BigIntType(boolean nullable) {
        super(nullable);
    }
*/
    private BigIntType() {

    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onBigIntType(this, parameter);
    }
}
