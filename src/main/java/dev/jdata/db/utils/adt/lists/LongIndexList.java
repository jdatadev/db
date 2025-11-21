package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.byindex.ILongByIndexView;

abstract class LongIndexList extends BaseLongIndexList implements ILongIndexList {

    LongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    LongIndexList(AllocationType allocationType, long value) {
        super(allocationType, value);
    }

    LongIndexList(AllocationType allocationType, long[] values) {
        super(allocationType, values);
    }

    LongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, values, numElements);
    }

    LongIndexList(AllocationType allocationType, ILongByIndexView toCopy, int numElements) {
        super(allocationType, toCopy, numElements);
    }

    LongIndexList(AllocationType allocationType, BaseLongIndexList toCopy) {
        super(allocationType, toCopy);
    }
}
