package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.allocators.BaseArrayAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.TrackingInstanceAllocator;
import dev.jdata.db.utils.checks.Checks;

abstract class MutableIntIndexList extends BaseIntIndexList implements IMutableIntIndexList {

    static abstract class MutableIntIndexListAllocator extends TrackingInstanceAllocator {

        abstract MutableIntIndexList allocateMutableIntIndexList(int minimumCapacity);
        abstract void freeMutableIntIndexList(MutableIntIndexList list);
    }

    static final class HeapMutableIntIndexListAllocator extends MutableIntIndexListAllocator {

        static final HeapMutableIntIndexListAllocator INSTANCE = new HeapMutableIntIndexListAllocator();

        private HeapMutableIntIndexListAllocator() {

        }

        @Override
        MutableIntIndexList allocateMutableIntIndexList(int minimumCapacity) {

            return new MutableIntIndexList(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
        }

        @Override
        public void freeMutableIntIndexList(MutableIntIndexList list) {

            Objects.requireNonNull(list);
        }
    }

    private static final class MutableIntIndexListArrayAllocator extends BaseArrayAllocator<MutableIntIndexList> {

        MutableIntIndexListArrayAllocator(AllocationType allocationType) {
            super(c -> new MutableIntIndexList(allocationType, c), l -> IElementsView.intNumElements(l.getNumElements()));
        }

        MutableIntIndexList allocateMutableIntIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeMutableIntIndexList(MutableIntIndexList list) {

            Objects.requireNonNull(list);

            freeArrayInstance(list);
        }
    }

    public static final class CacheMutableIntIndexListAllocator<T> extends MutableIntIndexListAllocator implements IAllocators {

        private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

        private final MutableIntIndexListArrayAllocator mutableIntIndexListArrayAllocator;

        public CacheMutableIntIndexListAllocator() {

            this.mutableIntIndexListArrayAllocator = new MutableIntIndexListArrayAllocator(ALLOCATION_TYPE);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("mutableIntIndexListArrayAllocator", RefType.INSTANTIATED, MutableObjectIndexList.class, mutableIntIndexListArrayAllocator);
        }

        @Override
        MutableIntIndexList allocateMutableIntIndexList(int minimumCapacity) {

            return mutableIntIndexListArrayAllocator.allocateMutableIntIndexList(minimumCapacity);
        }

        @Override
        public void freeMutableIntIndexList(MutableIntIndexList list) {

            mutableIntIndexListArrayAllocator.freeMutableIntIndexList(list);
        }
    }

    MutableIntIndexList(int initialCapacity) {
        this(AllocationType.HEAP, initialCapacity);
    }

    MutableIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    @Override
    public final void clear() {

        clearElements();
    }

    @Override
    public final long getCapacity() {

        return elementsArray.length;
    }

    @Override
    public final void addTail(int value) {

        final int arrayLength = elementsArray.length;

        if (numElements == arrayLength) {

            this.elementsArray = Arrays.copyOf(elementsArray, arrayLength * DEFAULT_CAPACITY_MULTIPLICATOR);
        }

        elementsArray[numElements ++] = value;
    }

    @Override
    public final void addTail(int ... values) {

        Checks.isNotEmpty(values);

        final int num = numElements;
        final int numValues = values.length;

        final int numTotal = num + numValues;

        int[] a = elementsArray;

        if (numTotal > a.length) {

            a = this.elementsArray = Arrays.copyOf(elementsArray, numTotal * DEFAULT_CAPACITY_MULTIPLICATOR);
        }

        System.arraycopy(values, 0, a, num, numValues);

        this.numElements = numTotal;
    }

    @Override
    public final int setAndReturnPrevious(long index, int value) {

        Checks.checkIndex(index, getNumElements());

        final int intIndex = IByIndexView.intIndex(index);

        final int result = elementsArray[intIndex];

        elementsArray[intIndex] = value;

        return result;
    }

    @Override
    public final int removeTailAndReturnValue() {

        if (isEmpty()) {

            throw new IllegalStateException();
        }

        final int result = getTail();

        -- numElements;

        return result;
    }

    private boolean removeAtMostOne(int value) {

        final boolean result;

        final int num = numElements;

        if (num == 1) {

            if (elementsArray[0] == value) {

                this.numElements = 0;

                result = true;
            }
            else {
                result = false;
            }
        }
        else {
            final int index = findAtMostOne(value);

            if (index != -1) {

                if (index == 0) {

                    Array.move(elementsArray, 1, num - 1, -1);
                }
                else if (index == num - 1) {

                }
                else {
                    Array.move(elementsArray, index + 1, num - index - 1, -1);
                }

                -- numElements;

                result = true;
            }
            else {
                result = false;
            }
        }

        return result;
    }

    private int findAtMostOne(long value) {

        final int num = numElements;

        final int[] a = elementsArray;

        int foundIndex = -1;

        for (int i = 0; i < num; ++ i) {

            if (a[i] == value) {

                if (foundIndex != -1) {

                    throw new IllegalStateException();
                }

                foundIndex = i;
            }
        }

        return foundIndex;
    }
}
