package dev.jdata.db.utils.adt.capacity;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

@FunctionalInterface
public interface IOuterInnerWithValuesInstantiator<T, U> {

    T instantiate(AllocationType allocationType, int outerCapacity, int innerCapacity, U values);
}
