package dev.jdata.db.schema.types;

public final class SmallIntType extends BaseIntegerType {

    public static final SmallIntType INSTANCE = new SmallIntType();

    private SmallIntType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onSmallIntType(this, parameter);
    }
}
