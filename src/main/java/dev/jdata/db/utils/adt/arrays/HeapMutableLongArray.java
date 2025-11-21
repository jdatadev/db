package dev.jdata.db.utils.adt.arrays;

final class HeapMutableLongArray extends MutableLongArray implements IHeapMutableLongArray {

    static HeapMutableLongArray create(AllocationType allocationType) {

        checkCreateParameters(allocationType, AllocationMechanism.HEAP);

        return new HeapMutableLongArray(allocationType);
    }

    static HeapMutableLongArray create(AllocationType allocationType, int initialCapacity) {

        checkCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapMutableLongArray(allocationType, initialCapacity);
    }

    static HeapMutableLongArray create(AllocationType allocationType, int initialCapacity, long clearValue) {

        checkCreateParameters(allocationType, AllocationMechanism.HEAP, initialCapacity);

        return new HeapMutableLongArray(allocationType, initialCapacity, clearValue);
    }

    private HeapMutableLongArray(AllocationType allocationType) {
        super(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    private HeapMutableLongArray(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    private HeapMutableLongArray(AllocationType allocationType, int initialCapacity, long clearValue) {
        super(allocationType, initialCapacity, clearValue);
    }

    private HeapMutableLongArray(AllocationType allocationType, MutableLongArray toCopy) {
        super(allocationType, toCopy);
    }
}
