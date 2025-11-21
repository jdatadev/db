package dev.jdata.db.utils.adt.sets;

import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.capacity.ICapacity;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.LongCapacityMutableElementsAllocator;

abstract class BaseLongCapacityMutableSetAllocator<T extends IMutableElements & IMutableSetType, U extends BaseArrayLargeSet<?> & ICapacity, V extends IMutableFrom>

        extends LongCapacityMutableElementsAllocator<T, U, Void, V> {

    protected abstract U allocateMutableLargeSet(long minimumCapacity);

    BaseLongCapacityMutableSetAllocator() {
        super(c -> null);
    }

    @Override
    protected final U allocateMutableInstance(LongFunction<Void> createElements, long minimumCapacity) {

        checkAllocateMutableInstanceParameters(createElements, minimumCapacity);

        return allocateMutableLargeSet(minimumCapacity);
    }
}
