package dev.jdata.db.schema.types;

public final class SmallIntType extends BaseIntegerType {

    public static final SmallIntType INSTANCE = new SmallIntType();

    private SmallIntType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onSmallIntType(this, parameter);
    }
}
