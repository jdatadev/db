package dev.jdata.db.utils.adt.lists;

public interface LongMutableMultiList<T> extends LongMultiList {

    long removeNode(T instance, long node, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter);
}
