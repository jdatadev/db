package dev.jdata.db.sql.parse.expression;

import java.util.Objects;

final class MutableLargeInteger implements LargeInteger {

    private byte[] bits;
    private int numBits;

    void setValue(CharSequence integerCharSequence) {

        Objects.requireNonNull(integerCharSequence);

        throw new UnsupportedOperationException();
    }
}
