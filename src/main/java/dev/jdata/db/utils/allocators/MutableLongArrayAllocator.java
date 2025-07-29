package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.arrays.MutableLongArray;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongArrayAllocator extends BaseArrayAllocator<MutableLongArray> implements IMutableLongArrayAllocator {

    public MutableLongArrayAllocator() {
        super(c -> new MutableLongArray(c), a -> Integers.checkUnsignedLongToUnsignedInt(a.getCapacity()));
    }

    @Override
    public MutableLongArray allocateLongArray(int minimumCapacity) {

        return allocateArrayInstance(minimumCapacity);
    }

    @Override
    public void freeLongArray(MutableLongArray longArray) {

        freeArrayInstance(longArray);
    }
}
