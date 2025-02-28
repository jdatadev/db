package dev.jdata.db.sql.parse.expression;

import java.util.Objects;

public final class SQLScratchIntegerValue {

    private long longInteger;
    private final MutableLargeInteger largeInteger;
    private final StringBuilder largeIntegerStringBuilder;

    private boolean isLongIntegerSet;
    private boolean isLargeIntegerSet;

    SQLScratchIntegerValue() {

        this.largeInteger = new MutableLargeInteger();

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

    LargeInteger getLargeInteger() {
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
