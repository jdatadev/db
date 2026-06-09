package dev.jdata.db.schema.types;

public final class IntegerType extends BaseIntegerType {

    public static final IntegerType INSTANCE = new IntegerType();

    private IntegerType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onIntegerType(this, parameter);
    }
}
