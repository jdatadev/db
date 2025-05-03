package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

abstract class BaseObjectMapAllocator<T> extends BaseArrayAllocator<T> {

    BaseObjectMapAllocator(IntFunction<T> createMap, ToIntFunction<T> mapCapacityGetter) {
        super(createMap, mapCapacityGetter);
    }
}
