package dev.jdata.db.utils.adt.decimals;
import java.math.BigDecimal;
import java.util.Objects;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.buffers.BitBuffer;
import dev.jdata.db.utils.adt.integers.ILargeInteger;

public final class MutableDecimal extends BaseDecimal<MutableDecimal> implements IMutableDecimal, Comparable<MutableDecimal>, IClearable {

    public static MutableDecimal ofPrecision(int precision) {

        return new MutableDecimal(precision, 0);
    }

    public static MutableDecimal valueOf(BigDecimal value) {

        return new MutableDecimal(value);
    }

    public MutableDecimal() {
        this(16, 0);
    }

    private MutableDecimal(BigDecimal value) {

        initialize(value);
    }

    private MutableDecimal(int precision, int scale) {
        super(precision, scale);
    }

    final void initialize(BitBuffer buffer, long bufferBitOffset, int precision, int scale) {

        initializeDecimal(buffer, bufferBitOffset, precision, scale);
    }

    @Override
    public int compareTo(MutableDecimal other) {

        return compareToOther(other);
    }

    @Override
    public void setValue(IDecimal decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(long integer) {

    }

    @Override
    public void setValue(double d) {

    }

    @Override
    public void add(IDecimal decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public void add(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void add(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void add(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void subtract(IDecimal decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public void subtract(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void subtract(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void subtract(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void multiply(IDecimal decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public void multiply(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void multiply(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void multiply(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void divide(IDecimal decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public void divide(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void divide(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void divide(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void modulus(IDecimal decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public void modulus(ILargeInteger largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public void modulus(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void modulus(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {

        clearImpl();
    }
}
