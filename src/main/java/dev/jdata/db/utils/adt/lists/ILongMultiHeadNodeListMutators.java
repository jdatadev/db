package dev.jdata.db.utils.adt.lists;

interface ILongMultiHeadNodeListMutators<T> extends INodeListMutatorsMarker {

    long addHead(T instance, long value, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
    long addTail(T instance, long value, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);

    long removeAtMostOneNodeByValue(T instance, long value, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
