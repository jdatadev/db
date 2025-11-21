package dev.jdata.db.utils.adt.lists;

interface IIntDoublyLinkedMultiHeadNodeListMutators<T> extends INodeListMutatorsMarker {

    int removeTailAndReturnValue(T instance, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);

    int removeNodeAndReturnValue(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
