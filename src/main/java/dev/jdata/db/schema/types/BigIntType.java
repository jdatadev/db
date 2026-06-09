package dev.jdata.db.schema.types;

public final class BigIntType extends BaseIntegerType {

    public static final BigIntType INSTANCE = new BigIntType();

    private BigIntType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onBigIntType(this, parameter);
    }
}
