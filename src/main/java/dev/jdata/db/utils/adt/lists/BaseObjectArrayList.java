package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.byindex.ByIndex;
import dev.jdata.db.utils.adt.byindex.IObjectByIndexView;
import dev.jdata.db.utils.adt.elements.IObjectElementsToString;
import dev.jdata.db.utils.adt.elements.IObjectForEach;
import dev.jdata.db.utils.adt.elements.IObjectForEachWithResult;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseObjectArrayList<T> extends BaseArrayList<T[]> implements IObjectElementsToString<T> {

    protected BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    protected BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, createElementsArray.apply(initialCapacity), 0);
    }

    protected BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, Array.of(instance, createElementsArray), 1);
    }

    protected BaseObjectArrayList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances, instances.length);
    }

    protected <U> BaseObjectArrayList(AllocationType allocationType, BaseObjectArrayList<T> toCopy) {
        this(allocationType, toCopy.createElementsArray, toCopy);
    }

    BaseObjectArrayList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances) {
        this(allocationType, createElementsArray, instances, instances.length);
    }

    BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    BaseObjectArrayList(AllocationType allocationType, T[] instances, int numElements) {
        super(allocationType, instances, numElements);
    }

    <U> BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, BaseObjectArrayList<U> toCopy, Function<U, T> mapper) {
        super(allocationType, createElementsArray, copy(createElementsArray, toCopy, mapper), toCopy.getIntNumElements());
    }

    BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IObjectByIndexView<T> toCopy, int numElements) {
        super(allocationType, createElementsArray, copy(createElementsArray, toCopy, numElements), numElements);
    }

    <U> BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, IObjectByIndexView<U> toCopy, int numElements, Function<U, T> mapper) {
        super(allocationType, createElementsArray, copy(createElementsArray, toCopy, numElements, mapper), numElements);
    }

    private <U> BaseObjectArrayList(AllocationType allocationType, IntFunction<T[]> createElementsArray, BaseObjectArrayList<T> toCopy) {
        super(allocationType, createElementsArray, copy(createElementsArray, toCopy), toCopy.getIntNumElements());
    }

    @Override
    public final <P> void toString(StringBuilder sb, P parameter, IElementsToStringAdder<T, P> consumer) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(consumer);

        ByIndex.toString(this, 0L, getNumElements(), sb, parameter, (l, i, b, p) -> consumer.addString(l.get(i), b, p));
    }

    public final T get(long index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= getIntNumElements()) {

            throw new IndexOutOfBoundsException();
        }

        return getElementsArray()[(int)index];
    }

    public final T getHead() {

        if (getIntNumElements() == 0) {

            throw new IllegalStateException();
        }

        return getElementsArray()[0];
    }

    public final T getTail() {

        final int numElements = getIntNumElements();

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        return getElementsArray()[numElements - 1];
    }

    public final <P> T findAtMostOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return Array.findAtMostOne(getElementsArray(), 0, getIntNumElements(), parameter, predicate);
    }

    public <P> T findExactlyOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return Array.findExactlyOne(getElementsArray(), 0, getIntNumElements(), parameter, predicate);
    }

    private long findAtMostOneInstanceIndex(T instance) {

        Objects.requireNonNull(instance);

        int foundIndex = -1;

        final int num =  getIntNumElements();
        final T[] elementsArray = getElementsArray();

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

    @Override
    protected final T[] copyValues(T[] elements, long startIndex, long numElements) {

        checkIntCopyValuesParameters(elements, elements.length, startIndex, numElements);

        return Arrays.copyOfRange(elements, intIndex(startIndex), intNumElements(numElements));
    }

    @Override
    protected final void initializeWithValues(T[] values, long numElements) {

        checkIntIntitializeWithValuesParameters(values, values.length, numElements);

        initializeArrayList(values, intNumElements(numElements));
    }

    private void recreateArrays() {

        recreateArray(getElementsArrayLength());
    }

    protected final int getElementsArrayLength() {

        return getElementsArray().length;
    }

    private void addHeadElement(T instance) {

        if (instance == null) {

            throw new NullPointerException();
        }

        T[] thisArray = getElementsArray();
        final int arrayLength = thisArray.length;

        final int num = getIntNumElements();

        final T[] dstArray = num == arrayLength ? recreateArray(increaseCapacity(arrayLength)) : thisArray;

        System.arraycopy(thisArray, 0, dstArray, 1, num);

        thisArray[0] = instance;

        incrementNumElements();
    }

    protected final void addTailElement(T instance) {

        Objects.requireNonNull(instance);

        final T[] thisArray = getElementsArray();
        final int arrayLength = thisArray.length;

        final int numElements = getIntNumElements();

        final T[] dstArray;

        if (numElements == arrayLength) {

            dstArray = recreateArray(allocateLength(arrayLength + 1));

            System.arraycopy(thisArray, 0, dstArray, 0, arrayLength);
        }
        else {
            dstArray = thisArray;
        }

        dstArray[numElements] = instance;

        incrementNumElements();
    }

    final void addTail(BaseObjectArrayList<T> baseArrayList) {

        Objects.requireNonNull(baseArrayList);

        final int thisNumElements = getIntNumElements();
        final int toAddNumElements = baseArrayList.getIntNumElements();
        final int requiredCapacity = thisNumElements + toAddNumElements;

        final T[] thisArray = getElementsArray();
        final int arrayLength = thisArray.length;

        final T[] dstArray;

        if (requiredCapacity > arrayLength) {

            dstArray = recreateArray(allocateLength(requiredCapacity));

            System.arraycopy(thisArray, 0, dstArray, 0, thisNumElements);
        }
        else {
            dstArray = thisArray;
        }

        System.arraycopy(baseArrayList.getElementsArray(), 0, thisArray, thisNumElements, toAddNumElements);

        increaseNumElements(toAddNumElements);
    }

    final void addTailElements(@SuppressWarnings("unchecked") T... instances) {

        addTailElements(instances, 0, instances.length);
    }

    final void addTailElements(T[] instances, int startIndex, int numElementsToAdd) {

        Checks.checkIntAddFromArray(instances, startIndex, numElementsToAdd);

        switch (numElementsToAdd) {

        case 0:
            throw new IllegalArgumentException();

        case 1:
            throw new UnsupportedOperationException();

        default:

            final T[] thisArray = getElementsArray();
            final int arrayLength = thisArray.length;

            final int numListElementsBeforeAdd = getIntNumElements();

            final int requiredArrayLength = numListElementsBeforeAdd + numElementsToAdd;

            final T[] dstArray;

            if (requiredArrayLength > arrayLength) {

                dstArray = recreateArray(increaseCapacity(requiredArrayLength));

                System.arraycopy(thisArray, 0, dstArray, 0, numListElementsBeforeAdd);
            }
            else {
                dstArray = thisArray;
            }

            System.arraycopy(instances, startIndex, dstArray, numListElementsBeforeAdd, numElementsToAdd);

            increaseNumElements(numElementsToAdd);
            break;
        }
    }

    public final void set(long index, T instance) {

        Objects.requireNonNull(instance);
        Checks.checkLongIndex(index, getIntNumElements());

        getElementsArray()[(int)index] = instance;
    }

    public final T removeHeadAndReturnValue() {

        return remove(0);
    }

    private void removeHead(int numToRemove) {

        if (numToRemove != 0L) {

            if (numToRemove == 1) {

                remove(0);
            }
            else {
                final int num = getIntNumElements();

                if (numToRemove == num) {

                    clearElements();
                }
                else {
                    Array.move(getElementsArray(), 0, numToRemove, - numToRemove);

                    decreaseNumElements(numToRemove);
                }
            }
        }
    }

    private T remove(long index) {

        final int num = getIntNumElements();

        Checks.checkLongIndex(index, num);

        final int intIndex = intIndex(index);

        final T result;

        switch (num) {

        case 0:
            throw new IllegalStateException();

        case 1:

            if (index != 0) {

                throw new IllegalArgumentException();
            }

            result = getElementsArray()[intIndex];

            clearElements();
            break;

        default:

            final T[] elementsArray = getElementsArray();

            result = elementsArray[intIndex];

            if (index == 0) {

                Array.move(elementsArray, 0, num, -1);
            }
            else if (index == num - 1) {

            }
            else {
                Array.move(elementsArray, intIndex + 1, num - intIndex - 1, -1);
            }
            break;
        }

        decrementNumElements();

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

        Objects.requireNonNull(comparator);

        Arrays.sort(getElementsArray(), 0, getIntNumElements(), comparator);
    }

    public final <P, E extends Exception> void forEach(P parameter, IObjectForEach<T, P, E> forEach) throws E {

        final int num = getIntNumElements();
        final T[] elementsArray = getElementsArray();

        for (int i = 0; i < num; ++ i) {

            forEach.each(elementsArray[i], parameter);
        }
    }

    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IObjectForEachWithResult<T, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final int num = getIntNumElements();
        final T[] elementsArray = getElementsArray();

        for (int i = 0; i < num; ++ i) {

            final R forEachResult = forEach.each(elementsArray[i], parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    public final T get(int index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= getIntNumElements()) {

            throw new IndexOutOfBoundsException();
        }

        return getElementsArray()[index];
    }

    public final T set(int index, T element) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= getIntNumElements()) {

            throw new IndexOutOfBoundsException();
        }
        else if (element == null) {

            throw new NullPointerException();
        }

        getElementsArray()[index] = element;

        return element;
    }

    @Override
    protected final int getElementsCapacity() {

        return getElementsArrayLength();
    }

    final T[] elementsToArray(IntFunction<T[]> createArray) {

        final int num = getIntNumElements();

        final T[] result = createArray.apply(num);

        final T[] a = getElementsArray();

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

                return index < getIntNumElements();
            }

            @Override
            public T next() {

                if (index >= getIndexLimit()) {

                    throw new IndexOutOfBoundsException();
                }

                return getElementsArray()[index ++];
            }
        };
    }

    protected final <E> E[] makeArray(E[] dst) {

        final T[] a = getElementsArray();

        Checks.checkFromIndexSize(0, dst.length, a.length);

        final int num = getIntNumElements();

        for (int i = 0; i < num; ++ i) {

            @SuppressWarnings("unchecked")
            final E element = (E)a[i];

            dst[i] = element;
        }

        return dst;
    }

    private static <T> T[] copy(IntFunction<T[]> createElementsArray, BaseObjectArrayList<T> toCopy) {

        final int toCopyNumElements = toCopy.getIntNumElements();

        final T[] elementsArray = createElementsArray.apply(toCopyNumElements);

        System.arraycopy(toCopy.getElementsArray(), 0, elementsArray, 0, toCopyNumElements);

        return elementsArray;
    }

    private static <T, U> U[] copy(IntFunction<U[]> createElementsArray, BaseObjectArrayList<T> toCopy, Function<T, U> mapper) {

        final int toCopyNumElements = toCopy.getIntNumElements();

        final U[] elementsArray = createElementsArray.apply(toCopyNumElements);

        final T[] toCopyArray = toCopy.getElementsArray();

        for (int i = 0; i < toCopyNumElements; ++ i) {

            elementsArray[i] = mapper.apply(toCopyArray[i]);
        }

        return elementsArray;
    }

    private static <T> T[] copy(IntFunction<T[]> createElementsArray, IObjectByIndexView<T> toCopy, int toCopyNumElements) {

        final T[] elementsArray = createElementsArray.apply(toCopyNumElements);

        for (int i = 0; i < toCopyNumElements; ++ i) {

            elementsArray[i] = toCopy.get(i);
        }

        return elementsArray;
    }

    private static <T, U> U[] copy(IntFunction<U[]> createElementsArray, IObjectByIndexView<T> toCopy, int toCopyNumElements, Function<T, U> mapper) {

        final U[] elementsArray = createElementsArray.apply(toCopyNumElements);

        for (int i = 0; i < toCopyNumElements; ++ i) {

            elementsArray[i] = mapper.apply(toCopy.get(i));
        }

        return elementsArray;
    }

    private static int allocateLength(int requiredLength) {

        return requiredLength << 1;
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

            final int num = getIntNumElements();

            result = num == other.getIntNumElements() && Array.equals(getElementsArray(), 0, other.getElementsArray(), 0, num);
        }

        return result;
    }

    @Override
    public String toString() {

        return ByIndex.closureOrConstantToString(this, 0, getNumElements(), null, (e, i, b) -> b.append(e.get(i).toString()));
    }
}
