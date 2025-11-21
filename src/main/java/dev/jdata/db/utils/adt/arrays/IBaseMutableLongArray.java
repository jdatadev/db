package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;

interface IBaseMutableLongArray extends IMutableOneDimensionalArray, ILongArrayCommon, ILongArrayMutators, ILongAnyOrderAddable {

    @Override
    default void addInAnyOrder(long value) {

        add(value);
    }
}
