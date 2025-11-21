package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.lists.MutableIntIndexList.HeapMutableIntIndexListAllocator;

public final class HeapIntIndexList extends IntIndexList {

    public static final class HeapIntIndexListAllocator extends IntIndexListAllocator<HeapIntIndexList, HeapIntIndexListBuilder> {

        static final HeapIntIndexListAllocator INSTANCE = new HeapIntIndexListAllocator(HeapMutableIntIndexListAllocator.INSTANCE);

        private static final AllocationType ALLOCATION_TYPE = AllocationType.HEAP_ALLOCATOR;

        private final HeapMutableIntIndexListAllocator mutableIntIndexListAllocator;

        private HeapIntIndexListAllocator(HeapMutableIntIndexListAllocator mutableIntIndexListAllocator) {

            this.mutableIntIndexListAllocator = Objects.requireNonNull(mutableIntIndexListAllocator);
        }

        @Override
        public HeapIntIndexListBuilder allocateIntIndexListBuilder(int minimumCapacity) {

            return new HeapIntIndexListBuilder(ALLOCATION_TYPE, minimumCapacity, this);
        }

        @Override
        public void freeIntIndexListBuilder(HeapIntIndexListBuilder builder) {

            Objects.requireNonNull(builder);
        }

        @Override
        HeapIntIndexList allocateIntIndexListFrom(int[] values, int numElements) {

            return new HeapIntIndexList(ALLOCATION_TYPE, values, numElements);
        }

        @Override
        public void freeIntIndexList(IntIndexList list) {

            Objects.requireNonNull(list);
        }

        @Override
        MutableIntIndexList allocateMutableIntIndexList(int minimumCapacity) {

            return mutableIntIndexListAllocator.allocateMutableIntIndexList(minimumCapacity);
        }

        @Override
        void freeMutableIntIndexList(MutableIntIndexList list) {

            mutableIntIndexListAllocator.freeMutableIntIndexList(list);
        }
    }

    public static final class HeapIntIndexListBuilder extends IntIndexListBuilder<HeapIntIndexList, HeapIntIndexListBuilder> {

        private HeapIntIndexListBuilder(AllocationType allocationType, int minimumCapacity, HeapIntIndexListAllocator listAllocator) {
            super(allocationType, minimumCapacity, listAllocator);
        }

        @Override
        HeapIntIndexList empty() {

            return HeapIntIndexList.empty();
        }
    }

    private static final HeapIntIndexList emptyList = new HeapIntIndexList(AllocationType.HEAP);

    public static HeapIntIndexList empty() {

        return emptyList;
    }

    public static HeapIntIndexList of(int value) {

        return new HeapIntIndexList(AllocationType.HEAP, value);
    }

    private HeapIntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private HeapIntIndexList(AllocationType allocationType, int value) {
        super(allocationType, value);
    }

    private HeapIntIndexList(AllocationType allocationType, int[] values, int numElements) {
        super(allocationType, values, numElements);
    }
}
