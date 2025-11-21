package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseMutableLongToObjectDynamicMapAllocator<V, M extends IBaseMutableLongToObjectDynamicMap<V>>

        extends BaseCapacityInstanceAllocator<M>
        implements IBaseMutableLongToObjectDynamicMapAllocator<V, M> {

    BaseMutableLongToObjectDynamicMapAllocator(IntFunction<M> createMap) {
        super(createMap, m -> CapacityExponents.computeIntCapacityExponentExact(m.getCapacity()));
    }

    @Override
    public final M allocateLongToObjectMap(int initialCapacityExponent) {

        Checks.isIntInitialCapacityExponent(initialCapacityExponent);

        return super.allocateFromFreeListOrCreateCapacityInstance(initialCapacityExponent);
    }

    @Override
    public final void freeLongToObjectMap(M map) {

        freeArrayInstance(map);
    }
}
