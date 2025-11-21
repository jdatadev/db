package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.checks.Checks;

public interface IMutableIntArray extends IBaseMutableIntArray {

    public static IMutableIntArray create(int initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return new MutableIntArray(initialCapacity);
    }

    public static IMutableIntArray create(int initialCapacity, int clearValue) {

        Checks.isInitialCapacity(initialCapacity);

        return new MutableIntArray(initialCapacity, clearValue);
    }
}
