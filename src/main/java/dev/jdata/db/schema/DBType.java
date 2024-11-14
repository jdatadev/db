package dev.jdata.db.schema;

import dev.jdata.db.utils.checks.Checks;

public enum DBType {

    INTEGER(1, 32);

    private final int minNumBits;
    private final int maxNumBits;

    private DBType(int minNumBits, int maxNumBits) {

        this.minNumBits = Checks.isNumBits(minNumBits);
        this.maxNumBits = Checks.isNumBits(maxNumBits);
    }

    public int getMinNumBits() {
        return minNumBits;
    }

    public int getMaxNumBits() {
        return maxNumBits;
    }
}
