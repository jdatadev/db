package dev.jdata.db.utils.adt.lists;

abstract class BaseValues<T, U extends BaseList<T, U, V>, V extends BaseValues<T, U, V>> {

    abstract void reallocateOuter(int newOuterLength);
    abstract void allocateInner(int outerIndex, int innerCapacity);

    abstract T toArray(U list, long headNode, int numElements);
}
