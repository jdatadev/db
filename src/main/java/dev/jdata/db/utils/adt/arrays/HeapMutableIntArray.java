package dev.jdata.db.utils.adt.arrays;

final class HeapMutableIntArray extends MutableIntArray implements IHeapMutableIntArray {

    static HeapMutableIntArray create(AllocationType allocationType, int initialCapacity) {

        checkCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapMutableIntArray(allocationType, initialCapacity);
    }

    static HeapMutableIntArray create(AllocationType allocationType, int initialCapacity, int clearValue) {

        checkCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapMutableIntArray(allocationType, initialCapacity, clearValue);
    }

    private HeapMutableIntArray(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    private HeapMutableIntArray(AllocationType allocationType, int initialCapacity, int clearValue) {
        super(allocationType, initialCapacity, clearValue);
    }
}
