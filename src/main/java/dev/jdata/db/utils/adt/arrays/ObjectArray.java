package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class ObjectArray<T> implements IObjectArray<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_OBJECT_ARRAY;

    private static final Class<?> debugClass = ObjectArray.class;

    @SafeVarargs
    public static <T> ObjectArray<T> of(T ... instances) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("instances", instances));
        }

        final ObjectArray<T> result = new ObjectArray<>(instances);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    public static <T> ObjectArray<T> copyOf(ObjectArray<T> toCopy) {

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("toCopy", toCopy));
        }

        final ObjectArray<T> result = new ObjectArray<>(toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final T[] elements;

    private ObjectArray(T[] elements) {

        this.elements = Array.copyOf(elements);
    }

    private ObjectArray(ObjectArray<T> toCopy) {

        this.elements = Array.copyOf(toCopy.elements);
    }

    @Override
    public boolean isEmpty() {

        return elements.length == 0;
    }

    @Override
    public T get(long index) {

        return elements[Integers.checkUnsignedLongToUnsignedInt(index)];
    }

    @Override
    public long getLimit() {

        return elements.length;
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [elements=" + Arrays.toString(elements) + "]";
    }
}
