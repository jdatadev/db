package dev.jdata.db.schema.types;

public final class BlobType extends LargeObjectType {

    public static final BlobType INSTANCE = new BlobType();

    private BlobType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onBlobType(this, parameter);
    }
}
