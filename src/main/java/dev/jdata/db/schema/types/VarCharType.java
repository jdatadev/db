package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class VarCharType extends StringType {

    private final int minLength;
    private final int maxLength;

    public static VarCharType of(/*boolean nullable, */ int maxLength) {

        Checks.isLengthAboveZero(maxLength);

        return new VarCharType(/*nullable, */ 0, maxLength);
    }

    public static VarCharType of(/*boolean nullable, */ int minLength, int maxLength) {

        Checks.isNotNegative(minLength);
        Checks.isLengthAboveZero(maxLength);

        return new VarCharType(/*nullable, */ minLength, maxLength);
    }

    private VarCharType(/*boolean nullable, */ int minLength, int maxLength) {
//        super(nullable);

        this.minLength = Checks.isNotNegative(minLength);
        this.maxLength = Checks.isLengthAboveZero(maxLength);
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
