package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public abstract class InheritableArrayAllocator<T> extends ArrayAllocator<T> {

    protected InheritableArrayAllocator(IntFunction<T> createInstance, ToIntFunction<T> capacityGetter) {
        super(createInstance, capacityGetter);
    }
}
