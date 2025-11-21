package dev.jdata.db.utils.adt.lists;

interface IIntMultiHeadNodeListMutators<T> extends INodeListMutatorsMarker {

    long addHead(T instance, int value, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
    long addTail(T instance, int value, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);

    long removeAtMostOneNodeByValue(T instance, int value, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
