package dev.jdata.db.utils.adt.sets;

import java.util.Objects;
import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.LongCapacityMutableElementsAllocator;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongCapacityMutableSetAllocator<T extends IMutableElements & IMutableSetType, U extends BaseLargeArraySet<?> & ICapacity>

        extends LongCapacityMutableElementsAllocator<T, U, Void> {

    protected abstract U allocateMutableSet(long minimumCapacity);

        BaseLongCapacityMutableSetAllocator() {
        super(c -> null);
    }

    @Override
    protected final U allocateMutableInstance(LongFunction<Void> createElements, long minimumCapacity) {

        Objects.requireNonNull(createElements);
        Checks.isLongMinimumCapacity(minimumCapacity);

        return allocateMutableSet(minimumCapacity);
    }
}
