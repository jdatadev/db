package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;
import dev.jdata.db.utils.allocators.BaseAllocatableArrayAllocator;
import dev.jdata.db.utils.allocators.BaseArrayAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.ICacheable;
import dev.jdata.db.utils.scalars.Integers;

public final class CachedLongIndexList extends LongIndexList implements ICacheable {

    private static final class ArrayLongIndexListBuilderAllocator extends BaseAllocatableArrayAllocator<CachedLongIndexListBuilder> {

        ArrayLongIndexListBuilderAllocator(AllocationType allocationType, CacheLongIndexListAllocator listAllocator) {
            super(c -> new CachedLongIndexListBuilder(allocationType, c, listAllocator), l -> Integers.checkUnsignedLongToUnsignedInt(l.getCapacity()));
        }

        CachedLongIndexListBuilder allocateIndexListBuilder(int minimumCapacity) {

            return allocateAllocatableArrayInstance(minimumCapacity);
        }

        void freeIndexListBuilder(CachedLongIndexListBuilder builder) {

            freeAllocatableArrayInstance(builder);
        }
    }

    private static final class LongIndexListArrayAllocator extends BaseArrayAllocator<CachedLongIndexList> {

        LongIndexListArrayAllocator(AllocationType allocationType) {
            super(c -> new CachedLongIndexList(allocationType, c), l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        CachedLongIndexList allocateIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeIndexList(CachedLongIndexList list) {

            freeArrayInstance(list);
        }
    }

    private static final class MutableLongIndexListArrayAllocator extends BaseArrayAllocator<MutableLongIndexList> {

        MutableLongIndexListArrayAllocator(AllocationType allocationType) {
            super(c -> new MutableLongIndexList(allocationType, c), l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        MutableLongIndexList allocateMutableIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeMutableIndexList(MutableLongIndexList list) {

            freeArrayInstance(list);
        }
    }

    public static final class CacheLongIndexListAllocator extends LongIndexListAllocator<CachedLongIndexList, CachedLongIndexListBuilder> implements IAllocators {

        private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

        private final ArrayLongIndexListBuilderAllocator listBuilderArrayAllocator;
        private final LongIndexListArrayAllocator listArrayAllocator;
        private final MutableLongIndexListArrayAllocator mutableListArrayAllocator;

        public CacheLongIndexListAllocator() {

            this.listBuilderArrayAllocator = new ArrayLongIndexListBuilderAllocator(ALLOCATION_TYPE, this);
            this.listArrayAllocator = new LongIndexListArrayAllocator(ALLOCATION_TYPE);
            this.mutableListArrayAllocator = new MutableLongIndexListArrayAllocator(ALLOCATION_TYPE);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("listBuilderArrayAllocator", RefType.INSTANTIATED, IndexListBuilder.class, listBuilderArrayAllocator);
            statisticsGatherer.addInstanceAllocator("listArrayAllocator", RefType.INSTANTIATED, IndexList.class, listArrayAllocator);
            statisticsGatherer.addInstanceAllocator("mutableListArrayAllocator", RefType.INSTANTIATED, MutableIndexList.class, mutableListArrayAllocator);
        }

        @Override
        public CachedLongIndexListBuilder allocateLongIndexListBuilder(int minimumCapacity) {

            return listBuilderArrayAllocator.allocateIndexListBuilder(minimumCapacity);
        }

        @Override
        public void freeLongIndexListBuilder(CachedLongIndexListBuilder builder) {

            listBuilderArrayAllocator.freeIndexListBuilder(builder);
        }

        @Override
        CachedLongIndexList allocateLongIndexListFrom(long[] values, int numElements) {

            final CachedLongIndexList result = listArrayAllocator.allocateIndexList(numElements);

            result.initialize(values, numElements);

            return result;
        }

        @Override
        public void freeLongIndexList(LongIndexList list) {

            listArrayAllocator.freeIndexList((CachedLongIndexList)list);
        }

        @Override
        MutableLongIndexList allocateMutableLongIndexList(int minimumCapacity) {

            return mutableListArrayAllocator.allocateMutableIndexList(minimumCapacity);
        }

        @Override
        void freeMutableLongIndexList(MutableLongIndexList list) {

            mutableListArrayAllocator.freeMutableIndexList(list);
        }
    }

    public static final class CachedLongIndexListBuilder extends LongIndexListBuilder<CachedLongIndexList, CachedLongIndexListBuilder> implements ICacheable {

        private CachedLongIndexListBuilder(AllocationType allocationType, int minimumCapacity, CacheLongIndexListAllocator listAllocator) {
            super(allocationType, minimumCapacity, listAllocator);
        }
    }

    private static final CachedLongIndexList emptyList = new CachedLongIndexList(AllocationType.HEAP);

    public static CachedLongIndexList empty() {

        return emptyList;
    }

    private CachedLongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private CachedLongIndexList(AllocationType allocationType, long value) {
        super(allocationType, value);
    }

    private CachedLongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, values, numElements);
    }
}
