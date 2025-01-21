package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.scalars.Integers;

public final class FreeList<T> extends BaseNumElements implements Freeing<T> {

    private T[] list;

    public FreeList(IntFunction<T[]> createArray) {

        Objects.requireNonNull(createArray);

        this.list = createArray.apply(10);
    }

    public T allocate() {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        decreaseNumElements();

        return list[numElements - 1];
    }

    @Override
    public void free(T instance) {

        Objects.requireNonNull(instance);

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        if (Array.containsInstance(list, 0, numElements, instance)) {

            throw new IllegalArgumentException();
        }

        final int listLength = list.length;

        if (numElements == listLength) {

            this.list = Arrays.copyOf(list, listLength * 2);
        }

        list[numElements] = instance;

        increaseNumElements();
    }
}
