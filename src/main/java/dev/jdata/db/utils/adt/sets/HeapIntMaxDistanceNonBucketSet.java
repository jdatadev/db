package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;

final class HeapIntMaxDistanceNonBucketSet extends IntMaxDistanceNonBucketSet implements IHeapIntSet {

    private static final IHeapIntSet emptySet = HeapIntEmptySet.empty();

    static IHeapIntSet empty() {

        return emptySet;
    }

    static HeapIntMaxDistanceNonBucketSet copyArray(AllocationType allocationType, int[] values, int startIndex, int numElements) {

        checkHeapCopyArrayParameters(allocationType, values, startIndex, numElements);

        return new HeapIntMaxDistanceNonBucketSet(allocationType, startIndex == 0 ? Arrays.copyOf(values, numElements) : Arrays.copyOfRange(values, startIndex, numElements));
    }

    HeapIntMaxDistanceNonBucketSet(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }
}
