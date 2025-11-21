package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.allocators.BaseArrayAllocator;

public final class MutableLongArrayAllocator extends BaseArrayAllocator<MutableLongArray> implements IMutableLongArrayAllocator {

    public MutableLongArrayAllocator() {
        super(c -> new IMutableLongArray.(c), a -> ICapacity.intCapacity(a.getCapacity()));
    }

    @Override
    public IMutableLongArray allocateLongArray(int minimumCapacity) {

        return allocateArrayInstance(minimumCapacity);
    }

    @Override
    public void freeLongArray(IMutableLongArray longArray) {

        freeArrayInstance(longArray);
    }
}
