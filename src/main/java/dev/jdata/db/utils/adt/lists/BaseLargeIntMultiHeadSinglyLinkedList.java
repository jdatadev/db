package dev.jdata.db.utils.adt.lists;

public abstract class BaseLargeIntMultiHeadSinglyLinkedList<
                INSTANCE,
                LIST extends BaseLargeIntMultiHeadSinglyLinkedList<INSTANCE, LIST, VALUES>,
                VALUES extends BaseIntValues<LIST, VALUES>>

        extends BaseLargeIntSinglyLinkedList<INSTANCE, LIST, VALUES> {

    protected BaseLargeIntMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<int[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final boolean contains(int value, long headNode) {

        return containsValue(value, headNode);
    }

    public final <P> long findNodeWithValue(int value, long headNode) {

        return findValueNode(value, headNode);
    }

    public final long addHead(INSTANCE instance, int value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long addTail(INSTANCE instance, int value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long removeNode(INSTANCE instance, long node, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter, LongNodeSetter<INSTANCE> tailNodeSetter) {

        removeNodeByFindingPreviousNode(instance, node, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(node);
    }

    public final long removeNodeByValue(INSTANCE instance, int value, long headNode, long tailNode, LongNodeSetter<INSTANCE> headNodeSetter,
            LongNodeSetter<INSTANCE> tailNodeSetter) {

        long previousNode = NO_NODE;

        long removedNode = BaseList.NO_NODE;

        for (long n = headNode; n != NO_NODE; n = getNextNode(n)) {

            if (getValue(n) == value) {

                removeNode(instance, n, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);

                removedNode = n;
                break;
            }

            previousNode = n;
        }

        return removedNode;
    }

    @Override
    final void clearNumElements() {

    }
}
