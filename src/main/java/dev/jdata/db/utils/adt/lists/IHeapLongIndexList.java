package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapLongIndexList extends ILongIndexList, IHeapContainsMarker {

    public static IHeapLongIndexList of(long ... values) {

        final IHeapLongIndexList result;

        switch (values.length) {

        case 0:
            result = HeapLongIndexList.empty();
            break;

        case 1:
            result = HeapLongIndexList.of(AllocationType.HEAP, values[0]);
            break;

        default:

            result = HeapLongIndexList. of(AllocationType.HEAP, values);
            break;
        }

        return result;
    }
}
