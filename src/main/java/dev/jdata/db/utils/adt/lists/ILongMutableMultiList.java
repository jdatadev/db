package dev.jdata.db.utils.adt.lists;

public interface ILongMutableMultiList<T> extends ILongMultiList {

    long removeNode(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter);
}
