package dev.jdata.db.schema.types;

public final class SmallIntType extends BaseIntegerType {

    public static final SmallIntType NULLABLE = new SmallIntType(true);
    public static final SmallIntType NON_NULLABLE = new SmallIntType(false);

    public static SmallIntType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private SmallIntType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onSmallIntType(this, parameter);
    }
}
