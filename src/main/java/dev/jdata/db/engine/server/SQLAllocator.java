package dev.jdata.db.engine.server;

import org.jutils.ast.objects.list.IAddableList;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.utils.adt.decimals.MutableDecimal;
import dev.jdata.db.utils.adt.integers.ILargeInteger;
import dev.jdata.db.utils.adt.integers.MutableLargeInteger;
import dev.jdata.db.utils.adt.lists.CachedLongIndexList.CacheLongIndexListAllocator;
import dev.jdata.db.utils.adt.lists.CachedLongIndexList.CachedLongIndexListBuilder;
import dev.jdata.db.utils.adt.lists.LongIndexList;
import dev.jdata.db.utils.allocators.AddableListAllocator;
import dev.jdata.db.utils.allocators.ArrayOfLongsAllocator;
import dev.jdata.db.utils.allocators.IAddableListAllocator;
import dev.jdata.db.utils.allocators.MutableDecimalAllocator;
import dev.jdata.db.utils.allocators.MutableLargeIntegerAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class SQLAllocator extends ObjectCacheNode implements ISQLAllocator {

    private final IAddableListAllocator listAllocator;
    private final CacheLongIndexListAllocator longIndexListAllocator;
    private final ArrayOfLongsAllocator arrayOfLongsAllocator;
    private final MutableDecimalAllocator mutableDecimalAllocator;
    private final MutableLargeIntegerAllocator mutableLargeIntegerAllocator;

    public SQLAllocator() {

        this.listAllocator = new AddableListAllocator();
        this.longIndexListAllocator = new CacheLongIndexListAllocator();
        this.arrayOfLongsAllocator = new ArrayOfLongsAllocator();
        this.mutableDecimalAllocator = new MutableDecimalAllocator();
        this.mutableLargeIntegerAllocator = new MutableLargeIntegerAllocator();
    }

    @Override
    public <T> IAddableList<T> allocateList(int minimumCapacity) {

        return listAllocator.allocateList(minimumCapacity);
    }

    @Override
    public void freeList(IAddableList<?> list) {

        listAllocator.freeList(list);
    }

    @Override
    public CachedLongIndexListBuilder allocateLongIndexListBuilder(int minimumCapacity) {

        return LongIndexList.createBuilder(longIndexListAllocator);
    }

    @Override
    public void freeLongIndexListBuilder(CachedLongIndexListBuilder builder) {

        longIndexListAllocator.freeLongIndexListBuilder(builder);
    }

    @Override
    public void freeLongIndexList(LongIndexList list) {

        longIndexListAllocator.freeLongIndexList(list);
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
