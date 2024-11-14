package dev.jdata.db.storage.backend;

import dev.jdata.db.utils.checks.Checks;

public enum StorageMode {

    INT_REFERENCE(NumStorageBits.INT_MIN, NumStorageBits.INT_MAX),
    LONG_REFERENCE(NumStorageBits.LONG_MIN, NumStorageBits.LONG_MAX),
    VERBATIM(8, Integer.MAX_VALUE);

    private final int minNumBits;
    private final int maxNumBits;

    private StorageMode(int minNumBits, int maxNumBits) {

        this.minNumBits = Checks.isNumBits(minNumBits);
        this.maxNumBits = Checks.isNumBits(maxNumBits);
    }

    int getMinNumBits() {
        return minNumBits;
    }

    int getMaxNumBits() {
        return maxNumBits;
    }
}
