package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class CharType extends StringType {

    private final int length;

    public static CharType of(int length) {

        Checks.isLengthAboveZero(length);

        return new CharType(length);
    }

    private CharType(int length) {

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
