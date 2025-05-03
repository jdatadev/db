package dev.jdata.db.utils.adt.lists;

public abstract class BaseValues<
                LIST_T,
                LIST extends BaseList<LIST_T, LIST, VALUES>,
                VALUES extends BaseValues<LIST_T, LIST, VALUES>> {

    protected abstract void reallocateOuter(int newOuterLength);
    protected abstract void allocateInner(int outerIndex, int innerCapacity);

    protected abstract LIST_T toArray(LIST list, long headNode, int numElements);
}
