package dev.jdata.db.utils.adt.lists;

public interface ILongMutableMultiList<T> extends ILongMultiList {

    long removeNode(T instance, long node, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter);
}
