package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.allocators.IFreeing;

public final class FreeList<T> implements IFreeing<T>, IElements {

    private T[] list;
    private int numElements;

    public FreeList(IntFunction<T[]> createArray) {

        Objects.requireNonNull(createArray);

        this.list = createArray.apply(10);
        this.numElements = 0;
    }

    @Override
    public boolean isEmpty() {

        return numElements == 0;
    }

    @Override
    public long getNumElements() {

        return numElements;
    }

    public T allocate() {

        final T result;

        if (numElements != 0) {

            result = list[-- numElements];
        }
        else {
            result = null;
        }

        return result;
    }

    @Override
    public void free(T instance) {

        Objects.requireNonNull(instance);

        if (Array.containsInstance(list, instance, 0, numElements)) {

            throw new IllegalArgumentException();
        }

        final int listLength = list.length;

        if (numElements == listLength) {

            this.list = Arrays.copyOf(list, listLength * 2);
        }

        list[numElements ++] = instance;
    }
}
