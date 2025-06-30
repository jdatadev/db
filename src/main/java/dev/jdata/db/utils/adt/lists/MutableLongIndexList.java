package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.allocators.BaseArrayAllocator;
import dev.jdata.db.utils.allocators.ElementAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongIndexList extends BaseLongIndexList implements IMutableLongIndexList {

    static abstract class MutableLongIndexListAllocator extends ElementAllocator {

        abstract MutableLongIndexList allocateMutableLongIndexList(int minimumCapacity);
        abstract void freeMutableLongIndexList(MutableLongIndexList list);
    }

    static final class HeapMutableLongIndexListAllocator extends MutableLongIndexListAllocator {

        static final HeapMutableLongIndexListAllocator INSTANCE = new HeapMutableLongIndexListAllocator();

        private HeapMutableLongIndexListAllocator() {

        }

        @Override
        MutableLongIndexList allocateMutableLongIndexList(int minimumCapacity) {

            return new MutableLongIndexList(AllocationType.HEAP_ALLOCATOR, minimumCapacity);
        }

        @Override
        public void freeMutableLongIndexList(MutableLongIndexList list) {

            Objects.requireNonNull(list);
        }
    }

    private static final class MutableLongIndexListArrayAllocator extends BaseArrayAllocator<MutableLongIndexList> {

        MutableLongIndexListArrayAllocator(AllocationType allocationType) {
            super(c -> new MutableLongIndexList(allocationType, c), l -> Integers.checkUnsignedLongToUnsignedInt(l.getNumElements()));
        }

        MutableLongIndexList allocateMutableLongIndexList(int minimumCapacity) {

            return allocateArrayInstance(minimumCapacity);
        }

        void freeMutableLongIndexList(MutableLongIndexList list) {

            Objects.requireNonNull(list);

            freeArrayInstance(list);
        }
    }

    public static final class CacheMutableLongIndexListAllocator<T> extends MutableLongIndexListAllocator implements IAllocators {

        private static final AllocationType ALLOCATION_TYPE = AllocationType.CACHING_ALLOCATOR;

        private final MutableLongIndexListArrayAllocator mutableLongIndexListArrayAllocator;

        public CacheMutableLongIndexListAllocator() {

            this.mutableLongIndexListArrayAllocator = new MutableLongIndexListArrayAllocator(ALLOCATION_TYPE);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("mutableLongIndexListArrayAllocator", RefType.INSTANTIATED, MutableIndexList.class, mutableLongIndexListArrayAllocator);
        }

        @Override
        MutableLongIndexList allocateMutableLongIndexList(int minimumCapacity) {

            return mutableLongIndexListArrayAllocator.allocateMutableLongIndexList(minimumCapacity);
        }

        @Override
        public void freeMutableLongIndexList(MutableLongIndexList list) {

            mutableLongIndexListArrayAllocator.freeMutableLongIndexList(list);
        }
    }

    public MutableLongIndexList(int initialCapacity) {
        this(AllocationType.HEAP, initialCapacity);
    }

    MutableLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    @Override
    public void clear() {

        clearElements();
    }

    @Override
    public void addTail(long value) {

        final int arrayLength = elementsArray.length;

        if (numElements == arrayLength) {

            this.elementsArray = Arrays.copyOf(elementsArray, arrayLength * DEFAULT_CAPACITY_MULTIPLICATOR);
        }

        elementsArray[numElements ++] = value;
    }

    public void addTail(long ... values) {

        final int num = numElements;
        final int numValues = values.length;

        final int numTotal = num + numValues;

        long[] a = elementsArray;

        if (numTotal > a.length) {

            a = this.elementsArray = Arrays.copyOf(elementsArray, numTotal * DEFAULT_CAPACITY_MULTIPLICATOR);
        }

        System.arraycopy(values, 0, a, num, numValues);

        this.numElements = numTotal;
    }


    @Override
    public boolean removeAtMostOne(long value) {

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

        final long[] a = elementsArray;

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
