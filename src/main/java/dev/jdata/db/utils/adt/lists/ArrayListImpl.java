package dev.jdata.db.utils.adt.lists;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntFunction;

@Deprecated // currently not in use
public final class ArrayListImpl<T> extends BaseArrayList<T> implements List<T> {

    public ArrayListImpl(IntFunction<T[]> createArray) {
        super(createArray);
    }

    public ArrayListImpl(IntFunction<T[]> createArray, int initialCapacity) {
        super(createArray, initialCapacity);
    }

    @Override
    public final boolean contains(Object o) {

        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {

        return makeIterator();
    }

    @Override
    public Object[] toArray() {

        throw new UnsupportedOperationException();
    }

    @Override
    public <E> E[] toArray(E[] dst) {

        return makeArray(dst);
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
    public int size() {

        throw new UnsupportedOperationException();
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
}
