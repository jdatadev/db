package dev.jdata.db.schema.types;

public final class BlobType extends LargeObjectType {

    public static final BlobType INSTANCE = new BlobType();

/*
    public static final BlobType NULLABLE = new BlobType(true);
    public static final BlobType NON_NULLABLE = new BlobType(false);

    public static BlobType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private BlobType(boolean nullable) {
        super(nullable);
    }
*/

    private BlobType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onBlobType(this, parameter);
    }
}
