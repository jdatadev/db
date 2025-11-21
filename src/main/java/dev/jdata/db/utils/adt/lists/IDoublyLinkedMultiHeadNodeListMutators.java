package dev.jdata.db.utils.adt.lists;

interface IDoublyLinkedMultiHeadNodeListMutators<T> extends INodeListMutatorsMarker {

    void removeNode(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
