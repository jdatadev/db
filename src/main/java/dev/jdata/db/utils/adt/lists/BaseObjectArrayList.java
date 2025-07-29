package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IElementsToString;
import dev.jdata.db.utils.adt.elements.IObjectIterableElements.IForEach;
import dev.jdata.db.utils.adt.elements.IObjectIterableElements.IForEachWithResult;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseObjectArrayList<T> extends BaseArrayList<T[]> implements IElementsToString<T> {

    BaseObjectArrayList(AllocationType allocationType) {
        super(allocationType);
    }

    protected BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        this(allocationType, createElementsArray, DEFAULT_INITIAL_CAPACITY);
    }

    BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances) {
        this(allocationType, createElementsArray, instances.length);

        addTailElements(instances);
    }

    BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);

        if (numElements > instances.length) {

            throw new IllegalArgumentException();
        }
    }

    protected BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray);

        Objects.requireNonNull(createElementsArray);
        Objects.requireNonNull(instance);

        final int numElements = 1;

        this.elementsArray = createElementsArray.apply(numElements);
        elementsArray[0] = instance;

        this.numElements = numElements;
    }

    protected BaseObjectArrayList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances, instances.length);
    }

    protected BaseObjectArrayList(AllocationType allocationType, BaseObjectArrayList<T> toCopy) {
        super(allocationType, toCopy.createElementsArray);

        copy(createElementsArray, toCopy);
    }

    BaseObjectArrayList(IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {
        super(AllocationType.HEAP, createElementsArray);
    }

    BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IIndexList<T> toCopy) {
        super(allocationType, createElementsArray);

        if (toCopy instanceof BaseObjectArrayList<?>) {

            @SuppressWarnings("unchecked")
            final BaseObjectArrayList<T> indexList = (BaseObjectArrayList<T>)toCopy;

            copy(createElementsArray, indexList);
        }
        else {
            final int numElements = Integers.checkUnsignedLongToUnsignedInt(toCopy.getNumElements());

            this.elementsArray = createElementsArray.apply(numElements);

            for (int i = 0; i < numElements; ++ i) {

                elementsArray[i] = toCopy.get(i);
            }

            this.numElements = numElements;
        }
    }

    protected BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray);

        Objects.requireNonNull(createElementsArray);
        Checks.isInitialCapacity(initialCapacity);

        this.elementsArray = createElementsArray.apply(initialCapacity);
        this.numElements = 0;
    }

    protected BaseObjectArrayList(BaseObjectArrayList<T> toCopy) {
        super(AllocationType.HEAP, toCopy.createElementsArray);

        Objects.requireNonNull(toCopy);

        final int num = this.numElements = toCopy.numElements;
        final T[] toCopyArray = toCopy.elementsArray;
        final T[] thisArray = this.elementsArray = createElementsArray.apply(toCopyArray.length);

        System.arraycopy(toCopyArray, 0, thisArray, 0, num);
    }

    final void initialize(T[] values, int numElements) {

        initialize(values, numElements, values.length);
    }

    final void recreateArrays() {

        recreateArrays(elementsArray.length);
    }

    public final T get(long index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= numElements) {

            throw new IndexOutOfBoundsException();
        }

        return elementsArray[(int)index];
    }

    public final T getHead() {

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        return elementsArray[0];
    }

    public final T getTail() {

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        return elementsArray[numElements - 1];
    }

    public final <P> T findAtMostOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return Array.findAtMostOne(elementsArray, 0, numElements, parameter, predicate);
    }

    public <P> T findExactlyOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return Array.findExactlyOne(elementsArray, 0, numElements, parameter, predicate);
    }

    private long findAtMostOneInstanceIndex(T instance) {

        Objects.requireNonNull(instance);

        int foundIndex = -1;

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            if (elementsArray[i] == instance) {

                if (foundIndex != -1) {

                    throw new IllegalStateException();
                }

                foundIndex = i;
            }
        }

        return foundIndex;
    }

    protected final int getElementsArrayLength() {

        return elementsArray.length;
    }

    final void addHeadElement(T instance) {

        if (instance == null) {

            throw new NullPointerException();
        }

        T[] thisArray = elementsArray;
        final int arrayLength = thisArray.length;

        final int num = numElements;

        if (num == arrayLength) {

            final T[] newArray = createElementsArray.apply(increaseCapacity(arrayLength));

            System.arraycopy(thisArray, 0, newArray, 1, arrayLength);

            thisArray = this.elementsArray = newArray;
        }
        else {
            System.arraycopy(thisArray, 0, thisArray, 1, num);
        }

        thisArray[0] = instance;

        ++ numElements;
    }

    protected final void addTailElement(T instance) {

        if (instance == null) {

            throw new NullPointerException();
        }

        T[] thisArray = elementsArray;
        final int arrayLength = thisArray.length;

        if (numElements == arrayLength) {

            final T[] newArray = createElementsArray.apply(allocateLength(arrayLength + 1));

            System.arraycopy(thisArray, 0, newArray, 0, arrayLength);

            thisArray = this.elementsArray = newArray;
        }

        thisArray[numElements ++] = instance;
    }

    final void addTail(BaseObjectArrayList<T> baseArrayList) {

        final int thisNumElements = numElements;
        final int toAddNumElements = baseArrayList.numElements;
        final int requiredCapacity = thisNumElements + toAddNumElements;

        T[] thisArray = elementsArray;
        final int arrayLength = elementsArray.length;

        if (requiredCapacity > arrayLength) {

            final T[] newArray = createElementsArray.apply(allocateLength(requiredCapacity));

            System.arraycopy(thisArray, 0, newArray, 0, thisNumElements);

            thisArray = this.elementsArray = newArray;
        }

        System.arraycopy(baseArrayList.elementsArray, 0, thisArray, thisNumElements, toAddNumElements);

        this.numElements += toAddNumElements;
    }

    final void addTailElements(@SuppressWarnings("unchecked") T... instances) {

        Checks.areElements(instances, Checks::checkIsNotNull);

        switch (instances.length) {

        case 0:
            throw new IllegalArgumentException();

        case 1:
            throw new UnsupportedOperationException();

        default:

            T[] dstArray = elementsArray;
            final int arrayLength = dstArray.length;
            final int numInstances = instances.length;

            final int requiredArrayLength = numElements + numInstances;

            final int num = numElements;

            if (requiredArrayLength > arrayLength) {

                final T[] newArray = createElementsArray.apply(requiredArrayLength << 1);

                System.arraycopy(dstArray, 0, newArray, 0, num);

                this.elementsArray = dstArray = newArray;
            }

            System.arraycopy(instances, 0, dstArray, num, numInstances);

            this.numElements += numInstances;
            break;
        }
    }

    public final void set(long index, T instance) {

        Objects.requireNonNull(instance);
        Checks.checkIndex(index, numElements);

        elementsArray[(int)index] = instance;
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
                    final T[] a = elementsArray;

                    final int remaining = num - numToRemove;

                    System.arraycopy(a, numToRemove, a, 0, remaining);

                    this.numElements = remaining;
                }
            }
        }
    }

    public final T remove(long index) {

        final int num = numElements;

        Checks.checkIndex(index, num);

        final int intIndex = Integers.checkUnsignedLongToUnsignedInt(index);

        final T result;

        switch (num) {

        case 0:
            throw new IllegalStateException();

        case 1:

            if (index != 0) {

                throw new IllegalArgumentException();
            }

            result = elementsArray[intIndex];

            clear();
            break;

        default:

            final T[] a = elementsArray;

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

    public final boolean removeAtMostOneInstance(T instance) {

        Objects.requireNonNull(instance);

        final long index = findAtMostOneInstanceIndex(instance);

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

        Arrays.sort(elementsArray, 0, numElements, comparator);
    }

    public final <P, E extends Exception> void forEach(P parameter, IForEach<T, P, E> forEach) throws E {

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            forEach.each(elementsArray[i], parameter);
        }
    }

    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<T, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            final R forEachResult = forEach.each(elementsArray[i], parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
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

        return elementsArray[index];
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

        elementsArray[index] = element;

        return element;
    }

    protected final int getElementsCapacity() {

        return elementsArray.length;
    }

    final T[] elementsToArray(IntFunction<T[]> createArray) {

        final int num = numElements;

        final T[] result = createArray.apply(num);

        final T[] a = elementsArray;

        for (int i = 0; i < num; ++ i) {

            result[i] = a[i];
        }

        return result;
    }

    final T[] makeArrayCopy() {

        return elementsToArray(createElementsArray);
    }

    protected final Iterator<T> makeIterator() {

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

                return elementsArray[index ++];
            }
        };
    }

    protected final <E> E[] makeArray(E[] dst) {

        final T[] a = elementsArray;

        Checks.checkFromIndexSize(0, dst.length, a.length);

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            @SuppressWarnings("unchecked")
            final E element = (E)a[i];

            dst[i] = element;
        }

        return dst;
    }

    final int getIntNumElements() {

        return numElements;
    }

    private void copy(IntFunction<T[]> createElementsArray, BaseObjectArrayList<T> toCopy) {

        final int toCopyNumElements = numElements = toCopy.numElements;

        this.elementsArray = createElementsArray.apply(toCopyNumElements);

        System.arraycopy(toCopy.elementsArray, 0, elementsArray, 0, toCopyNumElements);
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

            elementsToStringAdder.addString(elementsArray[i], sb, parameter);
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
            final BaseObjectArrayList<?> other = (BaseObjectArrayList<?>)object;

            final int num = numElements;

            result = num == other.numElements && Array.equals(elementsArray, 0, other.elementsArray, 0, num);
        }

        return result;
    }
}
