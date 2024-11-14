package dev.jdata.db.schema.types;

import dev.jdata.db.utils.checks.Checks;

public final class CharType extends StringType {

    private final int length;

    public CharType(boolean nullable, int length) {
        super(nullable);

        this.length = Checks.isAboveZero(length);
    }

    public int getLength() {
        return length;
    }
}
