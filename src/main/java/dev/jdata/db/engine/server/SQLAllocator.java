package dev.jdata.db.engine.server;

import java.util.Objects;

import org.jutils.ast.objects.list.IAddableList;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.utils.adt.lists.ICachedLongIndexList;
import dev.jdata.db.utils.adt.lists.ICachedLongIndexListAllocator;
import dev.jdata.db.utils.adt.lists.ICachedLongIndexListBuilder;
import dev.jdata.db.utils.adt.numbers.decimals.ICachedMutableDecimal;
import dev.jdata.db.utils.adt.numbers.decimals.ICachedMutableDecimalAllocator;
import dev.jdata.db.utils.adt.numbers.integers.ICachedMutableLargeInteger;
import dev.jdata.db.utils.adt.numbers.integers.ICachedMutableLargeIntegerAllocator;
import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;
import dev.jdata.db.utils.allocators.AddableListAllocator;
import dev.jdata.db.utils.allocators.IAddableListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jutils.ArrayOfLongsAllocator;

public final class SQLAllocator extends ObjectCacheNode implements ISQLAllocator {

    private final IAddableListAllocator listAllocator;
    private final ICachedLongIndexListAllocator longIndexListAllocator;
    private final ArrayOfLongsAllocator arrayOfLongsAllocator;
    private final ICachedMutableDecimalAllocator mutableDecimalAllocator;
    private final ICachedMutableLargeIntegerAllocator mutableLargeIntegerAllocator;

    public SQLAllocator(AllocationType allocationType) {
        super(allocationType);

        this.listAllocator = new AddableListAllocator();
        this.longIndexListAllocator = ICachedLongIndexListAllocator.create();
        this.arrayOfLongsAllocator = new ArrayOfLongsAllocator();
        this.mutableDecimalAllocator = ICachedMutableDecimalAllocator.create();
        this.mutableLargeIntegerAllocator = ICachedMutableLargeIntegerAllocator.create();
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
    public ICachedLongIndexListBuilder createLongIndexListBuilder(int minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return longIndexListAllocator.createBuilder(minimumCapacity);
    }

    @Override
    public void freeLongIndexListBuilder(ICachedLongIndexListBuilder builder) {

        Objects.requireNonNull(builder);

        longIndexListAllocator.freeBuilder(builder);
    }

    @Override
    public void freeLongIndexList(ICachedLongIndexList list) {

        longIndexListAllocator.freeImmutable(list);
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
    public ICachedMutableDecimal allocateDecimal(long beforeDecimalPoint, long afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public ICachedMutableDecimal allocateDecimal(ILargeIntegerView beforeDecimalPoint, long afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public ICachedMutableDecimal allocateDecimal(long beforeDecimalPoint, ILargeIntegerView afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public ICachedMutableDecimal allocateDecimal(ILargeIntegerView beforeDecimalPoint, ILargeIntegerView afterDecimalPoint) {

        return mutableDecimalAllocator.allocateDecimal(beforeDecimalPoint, afterDecimalPoint);
    }

    @Override
    public void freeDecimal(ICachedMutableDecimal decimal) {

        mutableDecimalAllocator.freeDecimal(decimal);
    }

    @Override
    public ICachedMutableLargeInteger allocateLargeInteger(int precision) {

        return mutableLargeIntegerAllocator.allocateLargeInteger(precision);
    }

    @Override
    public void freeLargeInteger(ICachedMutableLargeInteger largeInteger) {

        mutableLargeIntegerAllocator.freeLargeInteger(largeInteger);
    }
}
