package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.adt.byindex.ILongByIndexView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

final class HeapLongIndexList extends LongIndexList implements IHeapLongIndexList {

    private static final IHeapLongIndexList emptyList = HeapLongEmptyIndexList.empty();

    static IHeapLongIndexList empty() {

        return emptyList;
    }

    private static HeapLongIndexList of(long value) {

        return new HeapLongIndexList(AllocationType.HEAP, value);
    }

    private static HeapLongIndexList copyArray(AllocationType allocationType, long[] values, int numElements) {

        checkHeapCopyArrayParameters(allocationType, values, numElements);

        return new HeapLongIndexList(allocationType, Arrays.copyOf(values, numElements));
    }

    static HeapLongIndexList copyArray(AllocationType allocationType, long[] values, int startIndex, int numElements) {

        checkHeapCopyArrayParameters(allocationType, values, startIndex, numElements);

        return new HeapLongIndexList(allocationType, startIndex == 0 ? Arrays.copyOf(values, numElements) : Arrays.copyOfRange(values, startIndex, numElements));
    }

    static HeapLongIndexList copyMutableIndexList(AllocationType allocationType, IMutableLongIndexList mutableIndexList) {

        checkHeapCopyMutableParameters(allocationType, mutableIndexList);

        final HeapLongIndexList result;

        if (mutableIndexList instanceof BaseLongIndexList) {

            final BaseLongIndexList baseLongIndexList = (BaseLongIndexList)mutableIndexList;

            result = new HeapLongIndexList(allocationType, baseLongIndexList);
        }
        else {
            result = new HeapLongIndexList(allocationType, mutableIndexList, IOnlyElementsView.intNumElements(mutableIndexList));
        }

        return result;
    }

    static HeapLongIndexList withArray(AllocationType allocationType, long[] values, int numElements) {

        checkHeapWithArrayParameters(allocationType, values, numElements);

        return new HeapLongIndexList(allocationType, values, numElements);
    }

    private HeapLongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private HeapLongIndexList(AllocationType allocationType, long value) {
        super(allocationType, value);
    }

    private HeapLongIndexList(AllocationType allocationType, long[] values) {
        super(allocationType, values);
    }

    private HeapLongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, values, numElements);
    }

    private HeapLongIndexList(AllocationType allocationType, ILongByIndexView toCopy, int numElements) {
        super(allocationType, toCopy, numElements);
    }

    private HeapLongIndexList(AllocationType allocationType, BaseLongIndexList toCopy) {
        super(allocationType, toCopy);
    }
}
