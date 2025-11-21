package dev.jdata.db.utils.adt.lists;

abstract class BaseInnerOuterNodeListValues<TO_ARRAY, LIST_ACCESSOR extends IInnerOuterNodeListInternal<TO_ARRAY>> extends BaseNodeListValues<TO_ARRAY, LIST_ACCESSOR> {

    protected abstract void reallocateOuter(int newOuterLength);
    protected abstract void allocateInner(int outerIndex, int innerCapacity);
}
