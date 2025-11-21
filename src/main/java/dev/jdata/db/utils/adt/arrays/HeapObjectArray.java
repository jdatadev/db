package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

final class HeapObjectArray<T> extends ObjectArray<T> implements IHeapObjectArray<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_HEAP_OBJECT_ARRAY;

    private static final Class<?> debugClass = HeapObjectArray.class;

    @SafeVarargs
    static <T> HeapObjectArray<T> of(AllocationType allocationType, T ... instances) {

        AllocationType.checkIsHeap(allocationType);
        Checks.isNotEmpty(instances);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("allocationType", allocationType).add("instances", instances));
        }

        final HeapObjectArray<T> result = new HeapObjectArray<>(allocationType, instances);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    static <T> HeapObjectArray<T> copyOf(AllocationType allocationType, IObjectArrayView<T> toCopy) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        final HeapObjectArray<T> result = new HeapObjectArray<>(allocationType, (BaseObjectArray<T>)toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private HeapObjectArray(AllocationType allocationType, T[] elementsArray) {
        super(allocationType, elementsArray, null);
    }

    private HeapObjectArray(AllocationType allocationType, BaseObjectArray<T> toCopy) {
        super(allocationType, toCopy);
    }
}
