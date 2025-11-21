package dev.jdata.db.utils.jutils;

import org.jutils.language.common.names.IArrayOfLongsAllocator;

import dev.jdata.db.utils.adt.arrays.InheritableArrayAllocator;

public final class ArrayOfLongsAllocator extends InheritableArrayAllocator<long[]> implements IArrayOfLongsAllocator {

    public ArrayOfLongsAllocator() {
        super(long[]::new, a -> a.length);
    }

    @Override
    public long[] allocateArrayOfLongs(int numElements) {

        return allocateFromFreeListOrCreateCapacityInstance(numElements);
    }

    @Override
    public void freeArrayOfLongs(long[] array) {

        freeArrayInstance(array);
    }
}
