package dev.jdata.db.engine.descriptorables;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.Contains;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.adt.lists.Freeing;

public abstract class BaseDescriptorables<T extends Enum<T> & State, U extends BaseDescriptorable<T>, V extends Freeing<U> & Contains> extends BaseNumElements {

    @FunctionalInterface
    protected interface DescriptorableFactory<T, R> {

        R create(T parameter);
    }

    public static final int NO_DESCRIPTOR = -1;

    private final V freeList;

    private U[] descriptorables;

    private int numArrayElements;

    BaseDescriptorables(V freeList, IntFunction<U[]> createArray) {

        this.freeList = Objects.requireNonNull(freeList);

        this.descriptorables = createArray.apply(10);

        this.numArrayElements = 0;
    }

    protected final U getDescriptorable(int descriptor) {

        Objects.checkIndex(descriptor, numArrayElements);

        return descriptorables[descriptor];
    }

    final <F, A> U addDescriptorable(F factoryParameter, A allocateParameter, DescriptorableFactory<F, U> descriptorableFactory, BiFunction<V, A, U> freeListAllocator) {

        Objects.requireNonNull(descriptorableFactory);

        int descriptor = Array.findIndex(descriptorables, 0, numArrayElements, e -> e == null);

        if (descriptor == -1) {

            if (numArrayElements == descriptorables.length) {

                this.descriptorables = Arrays.copyOf(descriptorables, numArrayElements * 2);
            }

            descriptor = numArrayElements ++;
        }

        final U result;

        if (!freeList.isEmpty()) {

            result = freeListAllocator.apply(freeList, allocateParameter);
        }
        else {
            result = descriptorableFactory.create(/* descriptor,*/ factoryParameter);
        }

        Objects.requireNonNull(result);

        descriptorables[descriptor] = result;

        increaseNumElements();

        return result;
    }

    protected final void removeDescriptorable(U descriptorable) {

        Objects.requireNonNull(descriptorable);

        descriptorables[descriptorable.getDescriptor()] = null;

        freeList.free(descriptorable);

        decreaseNumElements();
    }
}
