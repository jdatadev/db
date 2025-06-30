package dev.jdata.db.utils.adt.lists;

abstract class BaseIntegerIndexList<T> extends BaseArrayList<T> {

    BaseIntegerIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseIntegerIndexList(AllocationType allocationType, T array, int numElements) {
        super(allocationType, array, numElements);
    }
}
