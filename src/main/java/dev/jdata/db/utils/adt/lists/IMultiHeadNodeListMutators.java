package dev.jdata.db.utils.adt.lists;

interface IMultiHeadNodeListMutators<T> extends INodeListMutatorsMarker {

    void clear(T instance, long headNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
