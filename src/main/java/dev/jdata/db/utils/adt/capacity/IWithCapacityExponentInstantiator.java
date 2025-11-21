package dev.jdata.db.utils.adt.capacity;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

@FunctionalInterface
public interface IWithCapacityExponentInstantiator<T, P> {

    T instantiate(AllocationType allocationType, int capacityExponent, P parameter);
}
