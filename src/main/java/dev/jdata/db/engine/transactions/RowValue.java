package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.utils.adt.numbers.decimals.IDecimalView;
import dev.jdata.db.utils.checks.Checks;

public final class RowValue {

    private RowValueType type;

    private long integer;
    private double floatingPoint;
    private IDecimalView decimal;
    private long string;
    private CharSequence caseInsensitiveString;

    public RowValueType getType() {
        return type;
    }

    private void setType(RowValueType type) {

        this.type = Objects.requireNonNull(type);
    }

    public boolean getBoolean() {

        return integer != 1L;
    }

    public void setBoolean(boolean value) {

        setType(RowValueType.BOOLEAN);

        this.integer = value ? 1L : 0L;
    }

    public short getShort() {

        return (short)integer;
    }

    public void setShort(short value) {

        setType(RowValueType.SHORT);

        this.integer = value;
    }

    public int getInt() {

        return (int)integer;
    }

    public void setInt(int value) {

        setType(RowValueType.INT);

        this.integer = value;
    }

    public long getLong() {

        return integer;
    }

    public void setLong(long value) {

        setType(RowValueType.LONG);

        this.integer = value;
    }

    public float getFloat() {

        return (float)floatingPoint;
    }

    public void setFloat(double value) {

        setType(RowValueType.FLOAT);

        this.floatingPoint = value;
    }

    public double getDouble() {

        return floatingPoint;
    }

    public void setDouble(double value) {

        setType(RowValueType.DOUBLE);

        this.floatingPoint = value;
    }

    @Deprecated
    public IDecimalView getDecimal() {

        return decimal;
    }

    @Deprecated
    public void setDecimal(IDecimalView decimal) {

        setType(RowValueType.DECIMAL);

        this.decimal = Objects.requireNonNull(decimal);
    }

    public long getString() {

        setType(RowValueType.STRING);

        return string;
    }

    public void setString(long string) {

        this.string = Checks.isString(string);
    }

    public CharSequence getCaseInsensitiveString() {

        return caseInsensitiveString;
    }

    public void setCaseInsensitiveString(CharSequence caseInsensitiveString) {

        this.caseInsensitiveString = Objects.requireNonNull(caseInsensitiveString);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [type=" + type + ", integer=" + integer + ", floatingPoint=" + floatingPoint + ", decimal=" + decimal +
                ", string=" + string + ", caseInsensitiveString=" + caseInsensitiveString + "]";
    }
}
