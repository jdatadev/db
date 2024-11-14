package dev.jdata.db.schema.types;

public final class BlobType extends LargeObjectType {

    public static final BlobType NULLABLE = new BlobType(true);
    public static final BlobType NON_NULLABLE = new BlobType(false);

    public static BlobType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private BlobType(boolean nullable) {
        super(nullable);
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onBlobType(this, parameter);
    }
}
