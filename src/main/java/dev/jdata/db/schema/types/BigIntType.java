package dev.jdata.db.schema.types;

public final class BigIntType extends BaseIntegerType {

    public static final BigIntType INSTANCE = new BigIntType();

    private BigIntType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onBigIntType(this, parameter);
    }
}
