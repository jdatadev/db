package dev.jdata.db.schema.types;

public final class BlobType extends LargeObjectType {

    public static final BlobType NULLABLE = new BlobType(true);
    public static final BlobType NON_NULLABLE = new BlobType(false);

    private BlobType(boolean nullable) {
        super(nullable);
    }
}
