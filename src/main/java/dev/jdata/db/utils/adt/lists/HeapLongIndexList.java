package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.lists.MutableLongIndexList.HeapMutableLongIndexListAllocator;

public final class HeapLongIndexList extends LongIndexList {

    public static final class HeapLongIndexListAllocator extends LongIndexListAllocator<HeapLongIndexList, HeapLongIndexListBuilder> {

        static final HeapLongIndexListAllocator INSTANCE = new HeapLongIndexListAllocator(HeapMutableLongIndexListAllocator.INSTANCE);

        private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

        private final HeapMutableLongIndexListAllocator mutableLongIndexListAllocator;

        private HeapLongIndexListAllocator(HeapMutableLongIndexListAllocator mutableLongIndexListAllocator) {

            this.mutableLongIndexListAllocator = Objects.requireNonNull(mutableLongIndexListAllocator);
        }

        @Override
        public HeapLongIndexListBuilder allocateLongIndexListBuilder(int minimumCapacity) {

            return new HeapLongIndexListBuilder(ALLOCATION_TYPE, minimumCapacity, this);
        }

        @Override
        public void freeLongIndexListBuilder(HeapLongIndexListBuilder builder) {

            Objects.requireNonNull(builder);
        }

        @Override
        HeapLongIndexList allocateLongIndexListFrom(long[] values, int numElements) {

            return new HeapLongIndexList(ALLOCATION_TYPE, values, numElements);
        }

        @Override
        public void freeLongIndexList(LongIndexList list) {

            Objects.requireNonNull(list);
        }

        @Override
        MutableLongIndexList allocateMutableLongIndexList(int minimumCapacity) {

            return mutableLongIndexListAllocator.allocateMutableLongIndexList(minimumCapacity);
        }

        @Override
        void freeMutableLongIndexList(MutableLongIndexList list) {

            mutableLongIndexListAllocator.freeMutableLongIndexList(list);
        }
    }

    public static final class HeapLongIndexListBuilder extends LongIndexListBuilder<HeapLongIndexList, HeapLongIndexListBuilder> {

        private HeapLongIndexListBuilder(AllocationType allocationType, int minimumCapacity, HeapLongIndexListAllocator listAllocator) {
            super(allocationType, minimumCapacity, listAllocator);
        }
    }

    private static final HeapLongIndexList emptyList = new HeapLongIndexList(AllocationType.HEAP);

    public static HeapLongIndexList empty() {

        return emptyList;
    }

    public static HeapLongIndexList of(long value) {

        return new HeapLongIndexList(AllocationType.HEAP, value);
    }

    private HeapLongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private HeapLongIndexList(AllocationType allocationType, long value) {
        super(allocationType, value);
    }

    private HeapLongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, values, numElements);
    }
}
