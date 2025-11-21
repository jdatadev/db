package dev.jdata.db.utils.adt.lists;

interface IObjectMultiHeadNodeListMutators<INSTANCE, T> extends INodeListMutatorsMarker {

    long addHead(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter);
    long addTail(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter);

    long removeAtMostOneNodeByValue(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter);
}
