package dev.jdata.db.schema.types;

public final class BlobType extends LargeObjectType {

    public static final BlobType INSTANCE = new BlobType();

    private BlobType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onBlobType(this, parameter);
    }
}
