package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IIterableElements.ForEach;
import dev.jdata.db.utils.adt.elements.IIterableElements.ForEach2;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseArrayList<T> extends Allocatable {

    static final int DEFAULT_INITIAL_CAPACITY = 10;

    private final IntFunction<T[]> createArray;
    private T[] array;
    private int numElements;

    @FunctionalInterface
    interface SwapToOtherFunction<T, R> {

        R apply(IntFunction<T[]> createArray, T[] array, int numElements);
    }

    final <R> R swapToOther(boolean clear, SwapToOtherFunction<T, R> swapToOtherFunction) {

        checkIsAllocated(clear);

        final R result = swapToOtherFunction.apply(createArray, array, numElements);

        if (clear) {

            final int numElements = DEFAULT_INITIAL_CAPACITY;

            this.array = createArray.apply(numElements);
            this.numElements = numElements;
        }
        else {
            this.array = null;
            this.numElements = 0;
        }

        return result;
    }

    BaseArrayList() {

        this.createArray = null;
        this.array = null;
        this.numElements = 0;
    }

    BaseArrayList(IntFunction<T[]> createArray) {
        this(createArray, DEFAULT_INITIAL_CAPACITY);
    }

    BaseArrayList(IntFunction<T[]> createArray, T[] instances) {
        this(createArray, instances.length);

        addTail(instances);
    }

    BaseArrayList(IntFunction<T[]> createArray, T[] instances, int numElements) {

        Objects.requireNonNull(createArray);
        Objects.requireNonNull(instances);
        Checks.isNumElements(numElements);

        if (numElements > instances.length) {

            throw new IllegalArgumentException();
        }

        this.createArray = createArray;
        this.array = instances;
        this.numElements = numElements;
    }

    protected BaseArrayList(IntFunction<T[]> createArray, T instance) {

        Objects.requireNonNull(createArray);
        Objects.requireNonNull(instance);

        final int numElements = 1;

        this.createArray = null;

        this.array = createArray.apply(numElements);
        array[0] = instance;

        this.numElements = numElements;
    }

    protected BaseArrayList(T[] instances) {

        this.createArray = null;
        this.array = dev.jdata.db.utils.adt.arrays.Array.copyOf(instances);
        this.numElements = instances.length;
    }

    protected BaseArrayList(BaseArrayList<T> toCopy) {

        this.createArray = toCopy.createArray;

        copy(createArray, toCopy);
    }

    BaseArrayList(IntFunction<T[]> createArray, IIndexList<T> toCopy) {

        this.createArray = createArray;

        if (toCopy instanceof BaseArrayList<?>) {

            @SuppressWarnings("unchecked")
            final BaseArrayList<T> indexList = (BaseArrayList<T>)toCopy;

            copy(createArray, indexList);
        }
        else {
            final int numElements = Integers.checkUnsignedLongToUnsignedInt(toCopy.getNumElements());

            this.array = createArray.apply(numElements);

            for (int i = 0; i < numElements; ++ i) {

                array[i] = toCopy.get(i);
            }

            this.numElements = numElements;
        }
    }

    protected BaseArrayList(IntFunction<T[]> createArray, int initialCapacity) {

        Objects.requireNonNull(createArray);
        Checks.isInitialCapacity(initialCapacity);

        this.createArray = createArray;

        this.array = createArray.apply(initialCapacity);
        this.numElements = 0;
    }

    public final long getNumElements() {

        return numElements;
    }

    public final T get(long index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= numElements) {

            throw new IndexOutOfBoundsException();
        }

        return array[(int)index];
    }

    public final T getHead() {

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        return array[0];
    }

    public final T getTail() {

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        return array[numElements - 1];
    }

    public final void addHead(T instance) {

        if (instance == null) {

            throw new NullPointerException();
        }

        T[] thisArray = array;
        final int arrayLength = thisArray.length;

        final int num = numElements;

        if (num == arrayLength) {

            final T[] newArray = createArray.apply(increaseCapacity(arrayLength));

            System.arraycopy(thisArray, 0, newArray, 1, arrayLength);

            thisArray = this.array = newArray;
        }
        else {
            System.arraycopy(thisArray, 0, thisArray, 1, num);
        }

        thisArray[0] = instance;

        ++ this.numElements;
    }

    public final void addTail(T instance) {

        if (instance == null) {

            throw new NullPointerException();
        }

        T[] thisArray = array;
        final int arrayLength = thisArray.length;

        if (numElements == arrayLength) {

            final T[] newArray = createArray.apply(allocateLength(arrayLength + 1));

            System.arraycopy(thisArray, 0, newArray, 0, arrayLength);

            thisArray = this.array = newArray;
        }

        thisArray[this.numElements ++] = instance;
    }

    final void addTail(BaseArrayList<T> baseArrayList) {

        final int thisNumElements = numElements;
        final int toAddNumElements = baseArrayList.numElements;
        final int requiredCapacity = thisNumElements + toAddNumElements;

        T[] thisArray = array;
        final int arrayLength = array.length;

        if (requiredCapacity > arrayLength) {

            final T[] newArray = createArray.apply(allocateLength(requiredCapacity));

            System.arraycopy(thisArray, 0, newArray, 0, thisNumElements);

            thisArray = this.array = newArray;
        }

        System.arraycopy(baseArrayList.array, 0, thisArray, thisNumElements, toAddNumElements);

        this.numElements += toAddNumElements;
    }

    public final void addTail(@SuppressWarnings("unchecked") T... instances) {

        switch (instances.length) {

        case 0:
            throw new IllegalArgumentException();

        case 1:
            throw new UnsupportedOperationException();

        default:

            T[] dstArray = array;
            final int arrayLength = dstArray.length;
            final int numInstances = instances.length;

            final int requiredArrayLength = numElements + numInstances;

            final int num = numElements;

            if (requiredArrayLength > arrayLength) {

                final T[] newArray = createArray.apply(requiredArrayLength << 1);

                System.arraycopy(dstArray, 0, newArray, 0, num);

                this.array = dstArray = newArray;
            }

            System.arraycopy(instances, 0, dstArray, num, numInstances);

            this.numElements += numInstances;
            break;
        }
    }

    public final void set(long index, T instance) {

        Objects.requireNonNull(instance);
        Objects.checkIndex(index, numElements);

        array[(int)index] = instance;
    }

    public final void sort(Comparator<? super T> comparator) {

        Arrays.sort(array, 0, numElements, comparator);
    }

    public final boolean isEmpty() {

        return numElements == 0;
    }

    public final <P, E extends Exception> void forEach(P parameter, ForEach<T, P, E> forEach) throws E {

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            forEach.each(array[i], parameter);
        }
    }

    public final <P1, P2, E extends Exception> void forEach(P1 parameter1, P2 parameter2, ForEach2<T, P1, P2, E> forEach) throws E {

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            forEach.each(array[i], parameter1, parameter2);
        }
    }

    public final void clear() {

        this.numElements = 0;
    }

    public final T get(int index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= numElements) {

            throw new IndexOutOfBoundsException();
        }

        return array[index];
    }

    public final T set(int index, T element) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= numElements) {

            throw new IndexOutOfBoundsException();
        }
        else if (element == null) {

            throw new NullPointerException();
        }

        array[index] = element;

        return element;
    }

    public final long getCapacity() {

        return array.length;
    }

    final IntFunction<T[]> getCreateArray() {
        return createArray;
    }

    final Iterator<T> makeIterator() {

        return new Iterator<T>() {

            private int index;

            {
                this.index = 0;
            }

            @Override
            public boolean hasNext() {

                return index < numElements;
            }

            @Override
            public T next() {

                if (index >= numElements) {

                    throw new IndexOutOfBoundsException();
                }

                return array[index ++];
            }
        };
    }

    final <E> E[] makeArray(E[] dst) {

        Objects.checkFromIndexSize(0, dst.length, this.array.length);

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            @SuppressWarnings("unchecked")
            final E element = (E)array[i];

            dst[i] = element;
        }

        return dst;
    }

    final int getIntNumElements() {

        return numElements;
    }

    private void copy(IntFunction<T[]> createArray, BaseArrayList<T> toCopy) {

        final int toCopyNumElements = this.numElements = toCopy.numElements;

        this.array = createArray.apply(toCopyNumElements);

        System.arraycopy(toCopy.array, 0, array, 0, toCopyNumElements);
    }

    private static int increaseCapacity(int capacity) {

        return capacity << 1;
    }

    private static int allocateLength(int requiredLength) {

        return requiredLength << 1;
    }
}
