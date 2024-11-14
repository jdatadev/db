package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class CharType extends StringType {

    private final int length;

    public static CharType of(boolean nullable, int length) {

        Checks.isLengthAboveZero(length);

        return new CharType(nullable, length);
    }

    private CharType(boolean nullable, int length) {
        super(nullable);

        this.length = Checks.isLengthAboveZero(length);
    }

    public int getLength() {
        return length;
    }

    @Override
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onCharType(this, parameter);
    }
}
