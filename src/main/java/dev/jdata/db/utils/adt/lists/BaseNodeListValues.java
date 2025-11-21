package dev.jdata.db.utils.adt.lists;

abstract class BaseNodeListValues<TO_ARRAY, LIST_ACCESSOR extends INodeListInternal<TO_ARRAY>> implements INodeListValuesMarker {

    static final long NO_NODE = BaseNodeList.NO_NODE;

    protected abstract TO_ARRAY toArray(LIST_ACCESSOR list, long headNode, int numElements);
}
