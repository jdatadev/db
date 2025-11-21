package dev.jdata.db.utils.adt.arrays;

final class CachedMutableLongArray extends MutableLongArray {

    static CachedMutableLongArray create(AllocationType allocationType, int initialCapacity) {

        checkCreateParameters(allocationType, AllocationMechanism.CACHE, initialCapacity);

        return new CachedMutableLongArray(allocationType, initialCapacity);
    }

    private CachedMutableLongArray(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }
}
