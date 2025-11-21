package dev.jdata.db.utils.allocators;

import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.function.ObjIntFunction;

public abstract class BaseIntCapacityInstanceAllocator<T> extends BaseCapacityInstanceAllocator<T> {

    protected <P> BaseIntCapacityInstanceAllocator(P parameter, ObjIntFunction<P, T> createInstance, ToLongFunction<T> capacityGetter) {
        super(CapacityMax.INT, parameter, (p, c) -> createInstance.apply(p, Capacity.intCapacityRenamed(c)), capacityGetter);
    }

    protected <P> BaseIntCapacityInstanceAllocator(P parameter, ObjIntFunction<P, T> createInstance, ToLongFunction<T> capacityGetter, boolean exactCapacityMatch) {
        super(CapacityMax.INT, parameter, (p, c) -> createInstance.apply(p, Capacity.intCapacityRenamed(c)), capacityGetter, exactCapacityMatch);
    }
}
