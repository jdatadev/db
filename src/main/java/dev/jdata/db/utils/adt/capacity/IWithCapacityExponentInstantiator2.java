package dev.jdata.db.utils.adt.capacity;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

@FunctionalInterface
public interface IWithCapacityExponentInstantiator2<T, P1, P2> {

    T instantiate(AllocationType allocationType, int capacityExponent, P1 parameter1, P2 parameter);
}
