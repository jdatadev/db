package dev.jdata.db.utils.adt.numbers.decimals;
import java.math.BigDecimal;
import java.util.Objects;

import dev.jdata.db.utils.adt.buffers.BitBuffer;
import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

abstract class MutableDecimal<T extends MutableDecimal<T>> extends BaseDecimal<T> implements IMutableDecimal {

    MutableDecimal(AllocationType allocationType, int precision) {
        super(allocationType, precision, 0);
    }

    MutableDecimal(AllocationType allocationType, int precision, int scale) {
        super(allocationType, precision, scale);
    }

    MutableDecimal(AllocationType allocationType, BigDecimal value) {
        super(allocationType);

        initialize(value);
    }

    private MutableDecimal(AllocationType allocationType) {
        this(allocationType, 16, 0);
    }

    final void initialize(BitBuffer buffer, long bufferBitOffset, int precision, int scale) {

        initializeDecimal(buffer, bufferBitOffset, precision, scale);
    }

    @Override
    public final void setValue(IDecimalView decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void setValue(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void setValue(long integer) {

    }

    @Override
    public final void setValue(double d) {

    }

    @Override
    public final void add(IDecimalView decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void add(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void subtract(IDecimalView decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void subtract(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void subtract(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void subtract(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void multiply(IDecimalView decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void multiply(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void multiply(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void multiply(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void divide(IDecimalView decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void divide(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void divide(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void divide(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void modulus(IDecimalView decimal) {

        Objects.requireNonNull(decimal);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void modulus(ILargeIntegerView largeInteger) {

        Objects.requireNonNull(largeInteger);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void modulus(long l) {

        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void modulus(double d) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void clear() {

        clearImpl();
    }
}
