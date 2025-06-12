package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IElementsToString;
import dev.jdata.db.utils.adt.elements.IIterableElements.ForEach;
import dev.jdata.db.utils.adt.elements.IIterableElements.ForEach2;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseArrayList<T> extends Allocatable implements IElementsToString<T> {

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

    BaseArrayList(AllocationType allocationType) {
        super(allocationType);

        this.createArray = null;
        this.array = null;
        this.numElements = 0;
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T[]> createArray) {
        this(allocationType, createArray, DEFAULT_INITIAL_CAPACITY);
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T[]> createArray, T[] instances) {
        this(allocationType, createArray, instances.length);

        addTail(instances);
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T[]> createArray, T[] instances, int numElements) {
        super(allocationType);

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

    protected BaseArrayList(AllocationType allocationType, IntFunction<T[]> createArray, T instance) {
        super(allocationType);

        Objects.requireNonNull(createArray);
        Objects.requireNonNull(instance);

        final int numElements = 1;

        this.createArray = null;

        this.array = createArray.apply(numElements);
        array[0] = instance;

        this.numElements = numElements;
    }

    protected BaseArrayList(AllocationType allocationType, T[] instances) {
        super(allocationType);

        this.createArray = null;
        this.array = dev.jdata.db.utils.adt.arrays.Array.copyOf(instances);
        this.numElements = instances.length;
    }

    protected BaseArrayList(AllocationType allocationType, BaseArrayList<T> toCopy) {
        super(allocationType);

        this.createArray = toCopy.createArray;

        copy(createArray, toCopy);
    }

    BaseArrayList(AllocationType allocationType, IntFunction<T[]> createArray, IIndexList<T> toCopy) {
        super(allocationType);

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

    protected BaseArrayList(AllocationType allocationType, IntFunction<T[]> createArray, int initialCapacity) {
        super(allocationType);

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

    public final long findInstanceIndex(T instance) {

        Objects.requireNonNull(instance);

        int foundIndex = -1;

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            if (array[i] == instance) {

                foundIndex = i;
                break;
            }
        }

        return foundIndex;
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

        ++ numElements;
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

        thisArray[numElements ++] = instance;
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

    public final T removeHead() {

        return remove(0);
    }

    public final void removeHead(long numElements) {

        Checks.isNumElements(numElements);

        if (numElements != 0L) {

            if (numElements == 1) {

                remove(0);
            }
            else {
                final int numToRemove = Integers.checkUnsignedLongToUnsignedInt(numElements);

                final int num = this.numElements;

                if (numToRemove == num) {

                    clear();
                }
                else {
                    final T[] a = array;

                    final int remaining = num - numToRemove;

                    System.arraycopy(a, numToRemove, a, 0, remaining);

                    this.numElements = remaining;
                }
            }
        }
    }

    public final T remove(long index) {

        final int num = numElements;

        Objects.checkIndex(index, num);

        final int intIndex = Integers.checkUnsignedLongToUnsignedInt(index);

        final T result;

        switch (num) {

        case 0:
            throw new IllegalStateException();

        case 1:

            if (index != 0) {

                throw new IllegalArgumentException();
            }

            result = array[intIndex];

            clear();
            break;

        default:

            final T[] a = array;

            result = a[intIndex];

            if (index == 0) {

                System.arraycopy(a, 1, a, 0, num - 1);
            }
            else if (index == num - 1) {

            }
            else {
                System.arraycopy(a, intIndex + 1, a, intIndex, num - intIndex - 1);
            }
            break;
        }

        -- numElements;

        return result;
    }

    public final boolean removeInstance(T instance) {

        Objects.requireNonNull(instance);

        final long index = findInstanceIndex(instance);

        final boolean removed;

        if (index == -1L) {

            removed = false;
        }
        else {
            remove(index);

            removed = true;
        }

        return removed;
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

        final int toCopyNumElements = numElements = toCopy.numElements;

        this.array = createArray.apply(toCopyNumElements);

        System.arraycopy(toCopy.array, 0, array, 0, toCopyNumElements);
    }

    private static int increaseCapacity(int capacity) {

        return capacity << 1;
    }

    private static int allocateLength(int requiredLength) {

        return requiredLength << 1;
    }

    @Override
    public final <P> void toString(StringBuilder sb, P parameter, ElementsToStringAdder<T, P> elementsToStringAdder) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(elementsToStringAdder);

        sb.append('[');

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            if (i > 0) {

                sb.append(',');
            }

            elementsToStringAdder.addString(array[i], sb, parameter);
        }

        sb.append(']');
    }

    @Override
    public final int hashCode() {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final BaseArrayList<?> other = (BaseArrayList<?>)object;

            final int num = numElements;

            result = num == other.numElements && Arrays.equals(array, 0, num, other.array, 0, num);
        }

        return result;
    }
}
