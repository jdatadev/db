package dev.jdata.db.sql.parse;

import org.jutils.parse.CharType;

import dev.jdata.db.utils.checks.Checks;

public final class NameCharType extends CharType {

    public static final NameCharType INSTANCE = new NameCharType();

    private NameCharType() {

    }

    @Override
    public boolean matches(CharSequence charSequence) {

        return Checks.checkIsDBName(charSequence);
    }
}