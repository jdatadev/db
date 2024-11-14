package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class VarCharType extends StringType {

    private final int minLength;
    private final int maxLength;

    public VarCharType(boolean nullable, int minLength, int maxLength) {
        super(nullable);

        this.minLength = Checks.isAboveZero(minLength);
        this.maxLength = Checks.isNotNegative(maxLength);
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
