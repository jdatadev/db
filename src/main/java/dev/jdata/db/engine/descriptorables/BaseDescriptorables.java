package dev.jdata.db.engine.descriptorables;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.allocators.IFreeing;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseDescriptorables<T extends Enum<T> & State, U extends BaseDescriptorable<T>, V extends IFreeing<U> & IContainsView> extends BaseNumElements<Void, Void, Void> {

    @FunctionalInterface
    protected interface IDescriptorableFactory<T, R> {

        R create(AllocationType allocationType, T parameter);
    }

    public static final int NO_DESCRIPTOR = -1;

    private final V freeList;

    private U[] descriptorables;

    private int numArrayElements;

    BaseDescriptorables(AllocationType allocationType, V freeList, IntFunction<U[]> createArray) {
        super(allocationType);

        this.freeList = Objects.requireNonNull(freeList);

        this.descriptorables = createArray.apply(10);

        this.numArrayElements = 0;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<Void, Void, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final Void copyValues(Void elements, long startIndex, long numElements) {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void initializeWithValues(Void values, long numElements) {

        throw new UnsupportedOperationException();
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

    final <F, A> U addDescriptorable(F factoryParameter, A allocateParameter, IDescriptorableFactory<F, U> descriptorableFactory, BiFunction<V, A, U> freeListAllocator) {

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
            result = descriptorableFactory.create(AllocationType.CACHING_ALLOCATOR, factoryParameter);
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
