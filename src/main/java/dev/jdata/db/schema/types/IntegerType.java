package dev.jdata.db.schema.types;

public final class IntegerType extends BaseIntegerType {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onIntegerType(this, parameter);
    }
}
