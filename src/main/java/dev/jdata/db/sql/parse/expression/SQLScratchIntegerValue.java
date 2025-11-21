package dev.jdata.db.sql.parse.expression;

import java.util.Objects;

import dev.jdata.db.utils.adt.numbers.integers.IHeapMutableLargeInteger;
import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

public final class SQLScratchIntegerValue {

    private long longInteger;
    private final IHeapMutableLargeInteger largeInteger;
    private final StringBuilder largeIntegerStringBuilder;

    private boolean isLongIntegerSet;
    private boolean isLargeIntegerSet;

    SQLScratchIntegerValue() {

        this.largeInteger = IHeapMutableLargeInteger.create();

        this.largeIntegerStringBuilder = new StringBuilder(100);
    }

    void clear() {

        this.isLongIntegerSet = false;
        this.isLargeIntegerSet = false;

        largeIntegerStringBuilder.setLength(0);
    }

    long getLongInteger() {
        return longInteger;
    }

    void setLongInteger(long longInteger) {

        checkIsNotSet(isLongIntegerSet);

        this.longInteger = longInteger;

        this.isLongIntegerSet = true;
    }

    boolean isLongIntegerSet() {
        return isLongIntegerSet;
    }

    ILargeIntegerView getLargeInteger() {
        return largeInteger;
    }

    void setLargeInteger(CharSequence integerCharSequence) {

        Objects.requireNonNull(integerCharSequence);

        checkIsNotSet(isLargeIntegerSet);

        largeInteger.setValue(integerCharSequence);

        this.isLargeIntegerSet = true;
    }

    boolean isLargeIntegerSet() {
        return isLargeIntegerSet;
    }

    StringBuilder getLargeIntegerStringBuilder() {
        return largeIntegerStringBuilder;
    }

    private static void checkIsNotSet(boolean isSet) {

        if (isSet) {

            throw new IllegalStateException();
        }
    }
}
