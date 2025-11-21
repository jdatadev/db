package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class CharType extends StringType {

    private final int length;

    public static CharType of(int length) {

        Checks.isIntLengthAboveZero(length);

        return new CharType(length);
    }

    private CharType(int length) {

        this.length = Checks.isIntLengthAboveZero(length);
    }

    public int getLength() {
        return length;
    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onCharType(this, parameter);
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (!super.equals(object)) {

            result = false;
        }
        else {
            final CharType other = (CharType)object;

            result = length == other.length;
        }

        return result;
    }
}
