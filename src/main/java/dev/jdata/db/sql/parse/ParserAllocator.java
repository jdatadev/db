package dev.jdata.db.sql.parse;

import java.util.List;

import org.jutils.language.common.names.Allocator;

import dev.jdata.db.utils.adt.arrays.LongArray;

public interface ParserAllocator extends Allocator {

    <T> List<T> allocateList(int initialCapacity);

    void freeList(List<?> list);

    LongArray allocateLongArray(int initialCapacity);
    void freeLongArray(LongArray longArray);
}
