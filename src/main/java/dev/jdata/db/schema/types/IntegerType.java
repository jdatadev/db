package dev.jdata.db.schema.types;

public final class IntegerType extends BaseIntegerType {

    public static final IntegerType INSTANCE = new IntegerType();

/*
    public static final IntegerType NULLABLE = new IntegerType(true);
    public static final IntegerType NON_NULLABLE = new IntegerType(false);

    public static IntegerType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private IntegerType(boolean nullable) {
        super(nullable);
    }
*/

    private IntegerType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onIntegerType(this, parameter);
    }
}
