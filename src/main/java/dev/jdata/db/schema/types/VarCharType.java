package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class VarCharType extends StringType {

    private final int minLength;
    private final int maxLength;

    public static VarCharType of(int maxLength) {

        Checks.isIntLengthAboveZero(maxLength);

        return new VarCharType(0, maxLength);
    }

    public static VarCharType of(int minLength, int maxLength) {

        Checks.isNotNegative(minLength);
        Checks.isIntLengthAboveZero(maxLength);

        return new VarCharType(minLength, maxLength);
    }

    private VarCharType(int minLength, int maxLength) {

        this.minLength = Checks.isNotNegative(minLength);
        this.maxLength = Checks.isIntLengthAboveZero(maxLength);
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onVarCharType(this, parameter);
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
            final VarCharType other = (VarCharType)object;

            result = minLength == other.minLength && maxLength == other.maxLength;
        }

        return result;
    }
}
