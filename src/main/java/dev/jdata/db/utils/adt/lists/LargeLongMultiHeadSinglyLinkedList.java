package dev.jdata.db.utils.adt.lists;

public final class LargeLongMultiHeadSinglyLinkedList<T> extends BaseLargeLongSinglyLinkedList<T> {

    public LargeLongMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity) {
        super(initialOuterCapacity, innerCapacity);
    }

    public boolean contains(long value, long headNode) {

        return containsValue(value, headNode);
    }

    public long addHead(T instance, long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public long addTail(T instance, long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public long removeNode(T instance, long node, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        removeNodeByFindingPreviousNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(node);
    }

    public boolean removeNodeByValue(T instance, long value, long headNode, long tailNode, LongNodeSetter<T> headNodeSetter, LongNodeSetter<T> tailNodeSetter) {

        long previousNode = NO_NODE;

        boolean removed = false;

        for (long n = headNode;; n = getNextNode(n)) {

            if (getValue(n) == value) {

                removeNode(instance, n, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);

                removed = true;
                break;
            }

            previousNode = n;
        }

        return removed;
    }

    @Override
    void clearNumElements() {

    }
}
