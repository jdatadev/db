package dev.jdata.db.schema.types;

public final class TextObjectType extends LargeObjectType {

    public static final TextObjectType INSTANCE = new TextObjectType();
/*
    public static final TextObjectType NULLABLE = new TextObjectType(true);
    public static final TextObjectType NON_NULLABLE = new TextObjectType(false);

    public static TextObjectType of(boolean nullable) {

        return nullable ? NULLABLE : NON_NULLABLE;
    }

    private TextObjectType(boolean nullable) {
        super(nullable);
    }
*/
    private TextObjectType() {

    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onTextObjectType(this, parameter);
    }
}
