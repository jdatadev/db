package dev.jdata.db.schema.types;

public final class TextObjectType extends LargeObjectType {

    public static final TextObjectType NULLABLE = new TextObjectType(true);
    public static final TextObjectType NON_NULLABLE = new TextObjectType(false);

    private TextObjectType(boolean nullable) {
        super(nullable);
    }
}
