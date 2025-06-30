package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseIntCapacityArrayTest<T extends IArray & IClearable> extends BaseArrayTest<T> {

    abstract T createArray(int initialCapacity);

    @Override
    final T createArray() {

        return createArray(1);
    }
}
