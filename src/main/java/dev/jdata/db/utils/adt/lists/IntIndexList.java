package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.byindex.IIntByIndexView;

public abstract class IntIndexList extends BaseIntIndexList implements IIntIndexList {

    IntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    IntIndexList(AllocationType allocationType, int value) {
        super(allocationType, value, false);
    }

    IntIndexList(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }

    IntIndexList(AllocationType allocationType, int[] values, int numElements) {
        super(allocationType, values, numElements);
    }

    IntIndexList(AllocationType allocationType, IIntByIndexView toCopy, int numElements) {
        super(allocationType, toCopy, numElements);
    }

    IntIndexList(AllocationType allocationType, BaseIntIndexList toCopy) {
        super(allocationType, toCopy);
    }
}
