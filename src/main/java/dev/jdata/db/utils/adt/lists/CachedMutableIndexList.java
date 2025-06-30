package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public final class CachedMutableIndexList<T> extends MutableIndexList<T> implements ICachedMutableIndexList<T> {

    public static final class CacheMutableIndexListAllocator<T> extends MutableIndexListAllocator<T, CachedMutableIndexList<T>> implements IAllocators {

        private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

        private final MutableIndexListArrayAllocator<T, CachedMutableIndexList<T>> mutableIndexListArrayAllocator;

        public CacheMutableIndexListAllocator(IntFunction<T[]> createElementsArray) {

            Objects.requireNonNull(createElementsArray);

            this.mutableIndexListArrayAllocator = new MutableIndexListArrayAllocator<>(ALLOCATION_TYPE, c -> new CachedMutableIndexList<>(ALLOCATION_TYPE, createElementsArray, c));
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("mutableIndexListArrayAllocator", RefType.INSTANTIATED, MutableIndexList.class, mutableIndexListArrayAllocator);
        }

        @Override
        CachedMutableIndexList<T> allocateMutableIndexList(int minimumCapacity) {

            return mutableIndexListArrayAllocator.allocateMutableIndexList(minimumCapacity);
        }

        @Override
        public void freeMutableIndexList(CachedMutableIndexList<T> list) {

            mutableIndexListArrayAllocator.freeMutableIndexList(list);
        }
    }

    private CachedMutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }
}
