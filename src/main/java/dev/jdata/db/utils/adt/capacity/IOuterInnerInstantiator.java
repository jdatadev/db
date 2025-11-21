package dev.jdata.db.utils.adt.capacity;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

@FunctionalInterface
public interface IOuterInnerInstantiator<T> {

    T instantiate(AllocationType allocationType, int outerCapacity, int innerCapacity);
}
