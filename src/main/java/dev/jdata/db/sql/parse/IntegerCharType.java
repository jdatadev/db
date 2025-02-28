package dev.jdata.db.sql.parse;

import org.jutils.parse.CharType;

public final class IntegerCharType extends CharType {

    public static final IntegerCharType INSTANCE = new IntegerCharType();

    private IntegerCharType() {

    }

    @Override
    protected boolean isOfType(char c) {

        return Character.isDigit(c);
    }
}
