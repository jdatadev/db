package dev.jdata.db.engine.descriptorables;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.allocators.IFreeing;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseDescriptorables<T extends Enum<T> & State, U extends BaseDescriptorable<T>, V extends IFreeing<U> & IContains> extends BaseNumElements {

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

        Checks.checkIndex(descriptor, numArrayElements);

        return descriptorables[descriptor];
    }

    protected final <P> void forEach(Consumer<U> consumer) {

        Objects.requireNonNull(consumer);

        forEach(consumer, (d, c) -> c.accept(d));
    }

    private <P> void forEach(P parameter, BiConsumer<U, P> consumer) {

        Objects.requireNonNull(consumer);

        final int numElements = numArrayElements;

        for (int i = 0; i < numElements; ++ i) {

            final U descriptorable = descriptorables[i];

            if (descriptorable != null) {

                consumer.accept(descriptorable, parameter);
            }
        }
    }

    final <F, A> U addDescriptorable(F factoryParameter, A allocateParameter, DescriptorableFactory<F, U> descriptorableFactory, BiFunction<V, A, U> freeListAllocator) {

        Objects.requireNonNull(descriptorableFactory);

        int descriptor = Array.closureOrConstantFindAtMostOneIndex(descriptorables, 0, numArrayElements, e -> e == null);

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
            result = descriptorableFactory.create(factoryParameter);
        }

        Objects.requireNonNull(result);

        descriptorables[descriptor] = result;

        incrementNumElements();

        return result;
    }

    protected final void removeDescriptorable(U descriptorable) {

        Objects.requireNonNull(descriptorable);

        descriptorables[descriptorable.getDescriptor()] = null;

        freeList.free(descriptorable);

        decrementNumElements();
    }

    protected final U removeDescriptorable(int descriptor) {

        Checks.isDescriptor(descriptor);

        final U descriptorable = getDescriptorable(descriptor);

        removeDescriptorable(descriptorable);

        return descriptorable;
    }
}
