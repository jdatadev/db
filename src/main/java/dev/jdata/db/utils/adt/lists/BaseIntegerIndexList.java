package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

abstract class BaseIntegerIndexList<T> extends BaseArrayList<T> {

    BaseIntegerIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    BaseIntegerIndexList(AllocationType allocationType, IntFunction<T> createElementsArray) {
        super(allocationType, createElementsArray);
    }

    BaseIntegerIndexList(AllocationType allocationType, T array, int numElements) {
        super(allocationType, array, numElements);
    }

    BaseIntegerIndexList(AllocationType allocationType, IntFunction<T> createElementsArray, T array, int numElements) {
        super(allocationType, createElementsArray, array, numElements);
    }
}
