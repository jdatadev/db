package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.checks.Checks;

public interface IMutableLongArray extends IBaseMutableLongArray {
heap
    public static IMutableLongArray create() {

        return Capacity.instantiateArray(MutableLongArray::new);
    }

    public static IMutableLongArray create(int initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return new MutableLongArray(initialCapacity);
    }
}
