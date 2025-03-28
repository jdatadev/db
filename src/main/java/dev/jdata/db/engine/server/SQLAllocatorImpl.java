package dev.jdata.db.engine.server;

import java.util.List;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.utils.adt.arrays.LongArray;
import dev.jdata.db.utils.adt.decimals.MutableDecimal;
import dev.jdata.db.utils.adt.integers.ILargeInteger;
import dev.jdata.db.utils.adt.integers.MutableLargeInteger;
import dev.jdata.db.utils.allocators.ArrayOfLongsAllocator;
import dev.jdata.db.utils.allocators.ListAllocator;
import dev.jdata.db.utils.allocators.LongArrayAllocator;
import dev.jdata.db.utils.allocators.MutableDecimalAllocator;
import dev.jdata.db.utils.allocators.MutableLargeIntegerAllocator;

final class SQLAllocatorImpl implements SQLAllocator {

    private final ListAllocator listAllocator;
    private final LongArrayAllocator longArrayAllocator;
    private final ArrayOfLongsAllocator arrayOfLongsAllocator;
    private final MutableDecimalAllocator mutableDecimalAllocator;
    private final MutableLargeIntegerAllocator mutableLargeIntegerAllocator;

    public SQLAllocatorImpl() {

        this.listAllocator = new ListAllocator(Object[]::new);
        this.longArrayAllocator = new LongArrayAllocator();
        this.arrayOfLongsAllocator = new ArrayOfLongsAllocator();
        this.mutableDecimalAllocator = new MutableDecimalAllocator();
        this.mutableLargeIntegerAllocator = new MutableLargeIntegerAllocator();
    }

    @Override
    public <T> List<T> allocateList(int minimumCapacity) {

        return listAllocator.allocateList(minimumCapacity);
    }

    @Override
    public void freeList(List<?> list) {

        listAllocator.freeList(list);
    }

    @Override
    public LongArray allocateLongArray(int minimumCapacity) {

        return longArrayAllocator.allocateLongArray(minimumCapacity);
    }

    @Override
    public void freeLongArray(LongArray longArray) {

        longArrayAllocator.freeLongArray(longArray);
    }

    @Override
    public long[] allocateArrayOfLongs(int numElements) {

        return arrayOfLongsAllocator.allocateArrayOfLongs(numElements);
    }

    @Override
    public void freeArrayOfLongs(long[] array) {

        arrayOfLongsAllocator.freeArrayOfLongs(array);
    }

    @Override
    public MutableDecimal allocateDecimal(long beforeDecimalPoint, long afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public MutableDecimal allocateDecimal(ILargeInteger beforeDecimalPoint, long afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public MutableDecimal allocateDecimal(long beforeDecimalPoint, ILargeInteger afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public MutableDecimal allocateDecimal(ILargeInteger beforeDecimalPoint, ILargeInteger afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public void freeDecimal(MutableDecimal decimal) {

        mutableDecimalAllocator.freeDecimal(decimal);
    }

    @Override
    public MutableLargeInteger allocateLargeInteger(int precision) {

        return mutableLargeIntegerAllocator.allocateLargeInteger(precision);
    }

    @Override
    public void freeLargeInteger(MutableLargeInteger largeInteger) {

        mutableLargeIntegerAllocator.freeLargeInteger(largeInteger);
    }
}
