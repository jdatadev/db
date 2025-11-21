package dev.jdata.db.utils.adt.lists;

public interface IMutableLongDoublyLinkedMultiHeadNodeList<T> extends IMutableLongMultiHeadNodeList<T>, ILongDoublyLinkedMultiHeadNodeListMutable<T> {

    @Override
    default long removeAtMostOneNodeByValue(T instance, long value, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter) {

        BaseNodeList.checkMultiHeadRemoveAtMostOneNodeByValueParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        final long node = findAtMostOneNode(value, headNode);

        if (node != LargeNodeLists.NO_LONG_NODE) {

            removeNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter);
        }

        return node;
    }
}
