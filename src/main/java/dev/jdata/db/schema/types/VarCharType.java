package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class VarCharType extends StringType {

    private final int minLength;
    private final int maxLength;

    public static VarCharType of(boolean nullable, int minLength, int maxLength) {

        Checks.isNotNegative(minLength);
        Checks.isLengthAboveZero(maxLength);

        return new VarCharType(nullable, minLength, maxLength);
    }

    private VarCharType(boolean nullable, int minLength, int maxLength) {
        super(nullable);

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
    public <T, R> R visit(SchemaDataTypeVisitor<T, R> visitor, T parameter) {

        return visitor.onVarCharType(this, parameter);
    }
}
