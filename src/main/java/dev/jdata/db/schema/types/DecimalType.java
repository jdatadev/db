package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class DecimalType extends BaseFractionalType {

    private final int precision;
    private final int scale;

    public static DecimalType of(/* boolean nullable, */ int precision, int scale) {

        Checks.isDecimalPrecision(precision);
        Checks.isDecimalScale(scale);

        return new DecimalType(/* nullable, */ precision, scale);
    }

    private DecimalType( /* boolean nullable, */ int precision, int scale) {
//        super(nullable);

        this.precision = Checks.isDecimalPrecision(precision);
        this.scale = Checks.isDecimalScale(scale);
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    @Override
    public <T, R, E extends Exception> R visit(SchemaDataTypeVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onDecimalType(this, parameter);
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
            final DecimalType other = (DecimalType)object;

            result = precision == other.precision && scale == other.scale;
        }

        return result;
    }
}
