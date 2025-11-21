package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

final class ObjectEmptyIndexList<T> extends ObjectEmptyList<T> implements IIndexList<T> {

    static final ObjectEmptyIndexList<?> INSTANCE = new ObjectEmptyIndexList<>();

    @Override
    public T get(long index) {

        Checks.checkIndex(index, 0L);

        return null;
    }

    @Override
    public IMutableIndexList<T> copyToMutable(IBaseIndexListAllocator<T, ? extends IBaseIndexList<T>, ?> indexListAllocator) {

        Objects.requireNonNull(indexListAllocator);

//        return indexListAllocator.allocateMutable(0);
    }

    @Override
    public long getIndexLimit() {

        return 0L;
    }
}
