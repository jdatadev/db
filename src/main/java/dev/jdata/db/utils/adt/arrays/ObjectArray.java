package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.debug.PrintDebug;

public final class ObjectArray<T> extends BaseObjectArray<T> implements IObjectArray<T> {

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

    private ObjectArray(T[] elements) {
        super(elements, elements.length, false);
    }

    private ObjectArray(ObjectArray<T> toCopy) {
        super(toCopy);
    }

    @Override
    T[] reallocate(T[] elements, int newCapacity) {

        throw new UnsupportedOperationException();
    }
}
