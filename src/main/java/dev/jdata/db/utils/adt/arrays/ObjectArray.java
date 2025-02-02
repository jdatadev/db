package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;

public final class ObjectArray<T> implements IObjectArray<T> {

    private final T[] elements;

    @SafeVarargs
    public static <T> ObjectArray<T> of(T ... instances) {

        return new ObjectArray<>(instances);
    }

    private ObjectArray(T[] elements) {

        this.elements = Array.copyOf(elements);
    }

    @Override
    public boolean isEmpty() {

        return elements.length == 0;
    }

    @Override
    public long getNumElements() {

        return elements.length;
    }

    @Override
    public T get(int index) {

        return elements[index];
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [elements=" + Arrays.toString(elements) + "]";
    }
}
