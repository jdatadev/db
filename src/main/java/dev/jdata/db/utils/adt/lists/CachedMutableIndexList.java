package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.allocators.BaseAllocatableArrayAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public final class CachedMutableIndexList<T> extends MutableObjectIndexList<T> implements ICachedMutableIndexList<T> {

    public static final class CacheMutableIndexListAllocator<T> extends MutableObjectIndexListAllocator<T, CachedMutableIndexList<T>> implements IAllocators {

        private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

        private final MutableIndexListArrayAllocator<T, CachedMutableIndexList<T>> mutableIndexListArrayAllocator;

        public CacheMutableIndexListAllocator(IntFunction<T[]> createElementsArray) {

            Objects.requireNonNull(createElementsArray);

            this.mutableIndexListArrayAllocator = new MutableIndexListArrayAllocator<>(ALLOCATION_TYPE, c -> new CachedMutableIndexList<>(ALLOCATION_TYPE, createElementsArray, c));
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("mutableIndexListArrayAllocator", RefType.INSTANTIATED, MutableObjectIndexList.class, mutableIndexListArrayAllocator);
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

    private static final class MutableIndexListArrayAllocator<T, U extends MutableObjectIndexList<T>> extends BaseAllocatableArrayAllocator<U> {

        MutableIndexListArrayAllocator(AllocationType allocationType, IntFunction<U> createList) {
            super(createList, l -> IElementsView.intNumElements(l.getNumElements()));
        }

        U allocateMutableIndexList(int minimumCapacity) {

            return allocateAllocatableArrayInstance(minimumCapacity);
        }

        void freeMutableIndexList(U list) {

            freeAllocatableArrayInstance(list);
        }
    }

    private CachedMutableIndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }
}
