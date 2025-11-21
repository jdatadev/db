package dev.jdata.db.utils.adt.lists;

interface ILongDoublyLinkedMultiHeadNodeListMutators<T> extends INodeListMutatorsMarker {

    long removeTailAndReturnValue(T instance, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);

    long removeNodeAndReturnValue(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
