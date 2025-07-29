package dev.jdata.db.utils.adt.lists;

public abstract class BaseLargeIntMultiHeadSinglyLinkedList<
                INSTANCE,
                LIST extends BaseLargeIntMultiHeadSinglyLinkedList<INSTANCE, LIST, VALUES>,
                VALUES extends BaseIntValues<LIST, VALUES>>

        extends BaseLargeIntSinglyLinkedList<INSTANCE, LIST, VALUES> {

    protected BaseLargeIntMultiHeadSinglyLinkedList(int initialOuterCapacity, int innerCapacity, ILargeListValuesFactory<int[], LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final boolean contains(int value, long headNode) {

        return containsValue(value, headNode);
    }

    public final <P> long findNodeWithValue(int value, long headNode) {

        return findValueNode(value, headNode);
    }

    public final long addHead(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long addTail(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    public final long removeNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        removeNodeByFindingPreviousNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return getValue(toRemove);
    }

    public final long removeNodeByValue(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long noNode = NO_NODE;

        long previousNode = noNode;
        long removedNode = noNode;

        for (long node = headNode; node != noNode; node = getNextNode(node)) {

            if (getValue(node) == value) {

                removeNode(instance, node, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);

                removedNode = node;
                break;
            }

            previousNode = node;
        }

        return removedNode;
    }

    @Override
    final void clearNumElements() {

    }
}
