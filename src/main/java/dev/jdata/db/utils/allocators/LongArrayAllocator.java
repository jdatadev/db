package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.arrays.LongArray;
import dev.jdata.db.utils.scalars.Integers;

public final class LongArrayAllocator extends BaseArrayAllocator<LongArray> implements ILongArrayAllocator {

    public LongArrayAllocator() {
        super(c -> new LongArray(c), a -> Integers.checkUnsignedLongToUnsignedInt(a.getCapacity()));
    }

    @Override
    public LongArray allocateLongArray(int minimumCapacity) {

        return allocateArrayInstance(minimumCapacity);
    }

    @Override
    public void freeLongArray(LongArray longArray) {

        freeArrayInstance(longArray);
    }
}
