package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.adt.byindex.IIntByIndexView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

final class HeapIntIndexList extends IntIndexList implements IHeapIntIndexList {

    private static final IHeapIntIndexList emptyList = HeapIntEmptyIndexList.empty();

    static IHeapIntIndexList empty() {

        return emptyList;
    }

    private static HeapIntIndexList of(int value) {

        return new HeapIntIndexList(AllocationType.HEAP, value);
    }

    private static HeapIntIndexList copyArray(AllocationType allocationType, int[] values, int numElements) {

        checkHeapCopyArrayParameters(allocationType, values, numElements);

        return new HeapIntIndexList(allocationType, Arrays.copyOf(values, numElements));
    }

    static HeapIntIndexList copyArray(AllocationType allocationType, int[] values, int startIndex, int numElements) {

        checkHeapCopyArrayParameters(allocationType, values, startIndex, numElements);

        return new HeapIntIndexList(allocationType, startIndex == 0 ? Arrays.copyOf(values, numElements) : Arrays.copyOfRange(values, startIndex, numElements));
    }

    static HeapIntIndexList copyMutableIndexList(AllocationType allocationType, IMutableIntIndexList mutableIndexList) {

        checkHeapCopyMutableParameters(allocationType, mutableIndexList);

        final HeapIntIndexList result;

        if (mutableIndexList instanceof BaseIntIndexList) {

            final BaseIntIndexList baseIntIndexList = (BaseIntIndexList)mutableIndexList;

            result = new HeapIntIndexList(allocationType, baseIntIndexList);
        }
        else {
            result = new HeapIntIndexList(allocationType, mutableIndexList, IOnlyElementsView.intNumElements(mutableIndexList));
        }

        return result;
    }

    static HeapIntIndexList withArray(AllocationType allocationType, int[] array, int numElements) {

        checkHeapWithArrayParameters(allocationType, array, numElements);

        return new HeapIntIndexList(allocationType, array, numElements);
    }

    private HeapIntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private HeapIntIndexList(AllocationType allocationType, int value) {
        super(allocationType, value);
    }

    private HeapIntIndexList(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }

    private HeapIntIndexList(AllocationType allocationType, int[] values, int numElements) {
        super(allocationType, values, numElements);
    }

    private HeapIntIndexList(AllocationType allocationType, IIntByIndexView toCopy, int numElements) {
        super(allocationType, toCopy, numElements);
    }

    private HeapIntIndexList(AllocationType allocationType, BaseIntIndexList toCopy) {
        super(allocationType, toCopy);
    }
}
