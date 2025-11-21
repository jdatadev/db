package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;

final class HeapIntMaxDistanceNonBucketSet extends IntMaxDistanceNonBucketSet implements IHeapIntSet {

    private static final IHeapIntSet emptySet = HeapIntEmptySet.empty();

    static IHeapIntSet empty() {

        return emptySet;
    }

    static HeapIntMaxDistanceNonBucketSet of(AllocationType allocationType, int ... values) {

        checkOfValuesParameters(allocationType, AllocationMechanism.HEAP, values, values.length);

        return new HeapIntMaxDistanceNonBucketSet(AllocationType.HEAP, values);
    }

    static HeapIntMaxDistanceNonBucketSet copyArray(AllocationType allocationType, int[] values, int startIndex, int numElements) {

        checkHeapCopyArrayParameters(allocationType, values, startIndex, numElements);

        return new HeapIntMaxDistanceNonBucketSet(allocationType, startIndex == 0 ? Arrays.copyOf(values, numElements) : Arrays.copyOfRange(values, startIndex, numElements));
    }

    private HeapIntMaxDistanceNonBucketSet(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }

    @Override
    public IHeapIntSet toHeapAllocated() {

        return this;
    }
}
