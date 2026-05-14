package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.checks.Checks;

public final class HeapObjectArrayAllocator<T> implements IArrayAllocator<T> {

    private final IntFunction<T[]> createArray;

    public HeapObjectArrayAllocator(IntFunction<T[]> createArray) {

        this.createArray = Objects.requireNonNull(createArray);
    }

    @Override
    public T[] allocateArray(int minimumCapacity) {

        Checks.isIntMinimumCapacityAtOrAboveZero(minimumCapacity);

        return createArray.apply(minimumCapacity);
    }

    @Override
    public void freeArray(T[] array) {

        Objects.requireNonNull(array);
    }
}
