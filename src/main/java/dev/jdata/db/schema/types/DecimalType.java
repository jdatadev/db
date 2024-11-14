package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class DecimalType {

    private final int precision;
    private final int scale;

    public DecimalType(int precision, int scale) {

        this.precision = Checks.isDecimalPrecision(precision);
        this.scale = Checks.isDecimalScale(scale);
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }
}
