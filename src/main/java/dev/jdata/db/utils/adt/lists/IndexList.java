package dev.jdata.db.utils.adt.lists;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class IndexList<T> implements List<T>, ICapacity, IMutableIndexList<T> {

    private final IntFunction<T[]> createArray;
    private T[] array;
    private int numElements;

    public static <T> IndexList<T> of(T instance) {

        Objects.requireNonNull(instance);

        @SuppressWarnings("unchecked")
        final IntFunction<T[]> createArray = l -> (T[])Array.newInstance(instance.getClass(), l);

        final IndexList<T> result = new IndexList<>(createArray);

        result.addHead(instance);

        return result;
    }

    public IndexList(IntFunction<T[]> createArray) {
        this(createArray, 10);
    }

    public IndexList(IntFunction<T[]> createArray, IIndexList<T> toCopy) {
        this(createArray, Integers.checkUnsignedLongToUnsignedInt(toCopy.getNumElements()));

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(toCopy.getNumElements());

        for (int i = 0; i < numElements; ++ i) {

            array[i] = toCopy.get(i);
        }

        this.numElements = numElements;
    }

    public IndexList(IntFunction<T[]> createArray, int initialCapacity) {

        Objects.requireNonNull(createArray);
        Checks.isInitialCapacity(initialCapacity);

        this.createArray = createArray;

        this.array = createArray.apply(initialCapacity);
        this.numElements = 0;
    }

    @Override
    public long getNumElements() {

        return numElements;
    }

    @Override
    public T get(long index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= numElements) {

            throw new IndexOutOfBoundsException();
        }

        return array[(int)index];
    }

    @Override
    public T getHead() {

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        return array[0];
    }

    @Override
    public T getTail() {

        if (numElements == 0) {

            throw new IllegalStateException();
        }

        return array[numElements - 1];
    }

    private static int increaseCapacity(int capacity) {

        return capacity << 1;
    }

    @Override
    public void addHead(T instance) {

        if (instance == null) {

            throw new NullPointerException();
        }

        T[] dstArray = array;
        final int arrayLength = dstArray.length;

        final int num = numElements;

        if (num == arrayLength) {

            final T[] newArray = createArray.apply(increaseCapacity(arrayLength));

            System.arraycopy(dstArray, 0, newArray, 1, arrayLength);

            this.array = dstArray = newArray;
        }
        else {
            System.arraycopy(dstArray, 0, dstArray, 1, num);
        }

        dstArray[0] = instance;

        ++ numElements;
    }

    @Override
    public void addTail(T instance) {

        if (instance == null) {

            throw new NullPointerException();
        }

        T[] dstArray = array;
        final int arrayLength = dstArray.length;

        if (numElements == arrayLength) {

            final T[] newArray = createArray.apply(arrayLength << 1);

            System.arraycopy(dstArray, 0, newArray, 0, arrayLength);

            this.array = dstArray = newArray;
        }

        dstArray[numElements ++] = instance;
    }

    @Override
    public int size() {

        return numElements;
    }

    @Override
    public boolean isEmpty() {

        return numElements == 0;
    }

    @Override
    public boolean contains(Object o) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {

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

    @Override
    public Object[] toArray() {

        throw new UnsupportedOperationException();
    }

    @Override
    public <E> E[] toArray(E[] dst) {

        Objects.checkFromIndexSize(0, dst.length, this.array.length);

        final int num = numElements;

        for (int i = 0; i < num; ++ i) {

            @SuppressWarnings("unchecked")
            final E element = (E)array[i];

            dst[i] = element;
        }

        return dst;
    }

    @Override
    public boolean add(T instance) {

        addTail(instance);

        return true;
    }

    @Override
    public boolean remove(Object o) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {

        this.numElements = 0;
    }

    @Override
    public T get(int index) {

        if (index < 0) {

            throw new IndexOutOfBoundsException();
        }
        else if (index >= numElements) {

            throw new IndexOutOfBoundsException();
        }

        return array[index];
    }

    @Override
    public T set(int index, T element) {

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

    @Override
    public void add(int index, T element) {

        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {

        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {

        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {

        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {

        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {

        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {

        throw new UnsupportedOperationException();
    }

    @Override
    public long getCapacity() {

        return array.length;
    }
}
