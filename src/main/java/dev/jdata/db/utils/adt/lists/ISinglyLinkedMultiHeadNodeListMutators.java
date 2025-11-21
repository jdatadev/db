package dev.jdata.db.utils.adt.lists;

interface ISinglyLinkedMultiHeadNodeListMutators<T> extends INodeListMutatorsMarker {

    void removeNodeByFindingPrevious(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
