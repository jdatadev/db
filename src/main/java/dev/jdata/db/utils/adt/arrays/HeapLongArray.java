package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.debug.PrintDebug;

final class HeapLongArray extends LongArray implements IHeapLongArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_HEAP_LONG_ARRAY;

    private static final Class<?> debugClass = HeapLongArray.class;

    static HeapLongArray of(AllocationType allocationType, long ... values) {

        checkOfValuesParameters(allocationType, AllocationMechanism.HEAP, values, values.length);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("allocationType", allocationType).add("values", values));
        }

        final HeapLongArray result = new HeapLongArray(allocationType, values);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    static HeapLongArray copyOf(AllocationType allocationType, ILongArrayView toCopy) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);

        Objects.requireNonNull(toCopy);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        final HeapLongArray result = new HeapLongArray(allocationType, (BaseLongArray)toCopy);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private HeapLongArray(AllocationType allocationType, long[] elementsArray) {
        super(allocationType, elementsArray);
    }

    private HeapLongArray(AllocationType allocationType, BaseLongArray toCopy) {
        super(allocationType, toCopy);
    }
}
