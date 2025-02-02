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
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onTextObjectType(this, parameter);
    }
}
