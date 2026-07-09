package dev.jdata.db.schema.types;

public final class TextObjectType extends LargeObjectType {

    public static final TextObjectType INSTANCE = new TextObjectType();

    private TextObjectType() {

    }

    @Override
    public <P, R, E extends Exception> R visit(SchemaDataTypeVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onTextObjectType(this, parameter);
    }
}
