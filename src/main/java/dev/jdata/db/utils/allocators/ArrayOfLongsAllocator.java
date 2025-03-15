package dev.jdata.db.utils.allocators;

import org.jutils.language.common.names.IArrayOfLongsAllocator;

public final class ArrayOfLongsAllocator extends BaseArrayAllocator<long[]> implements IArrayOfLongsAllocator {

    public ArrayOfLongsAllocator() {
        super(long[]::new, a -> a.length, true);
    }

    @Override
    public long[] allocateArrayOfLongs(int numElements) {

        return allocateArrayInstance(numElements);
    }

    @Override
    public void freeArrayOfLongs(long[] array) {

        freeArrayInstance(array);
    }
}
