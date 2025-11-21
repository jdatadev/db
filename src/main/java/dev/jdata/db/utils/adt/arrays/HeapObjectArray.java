package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.debug.PrintDebug;

final class HeapObjectArray<T> extends ObjectArray<T> implements IHeapObjectArray<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_HEAP_OBJECT_ARRAY;

    private static final Class<?> debugClass = HeapObjectArray.class;

    @SafeVarargs
    static <T> HeapObjectArray<T> of(AllocationType allocationType, T ... instances) {

        checkOfInstancesParameters(allocationType, AllocationMechanism.HEAP, instances);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("allocationType", allocationType).add("instances", instances));
        }

        final HeapObjectArray<T> result = new HeapObjectArray<>(allocationType, instances);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    static <T> HeapObjectArray<T> copyOf(AllocationType allocationType, IArrayView<T> toCopy) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        final HeapObjectArray<T> result = new HeapObjectArray<>(AllocationType.HEAP, (BaseObjectArray<T>)toCopy);

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
