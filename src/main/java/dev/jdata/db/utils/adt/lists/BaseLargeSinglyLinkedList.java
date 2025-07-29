package dev.jdata.db.utils.adt.lists;

public abstract class BaseLargeSinglyLinkedList<
                INSTANCE,
                LIST_T,
                LIST extends BaseLargeSinglyLinkedList<INSTANCE, LIST_T, LIST, VALUES>,
                VALUES extends BaseValues<LIST_T, LIST, VALUES>>

        extends BaseLargeList<LIST_T, LIST, VALUES> {

    protected BaseLargeSinglyLinkedList(int initialOuterCapacity, int innerCapacity, ILargeListValuesFactory<LIST_T, LIST, VALUES> valuesFactory) {
        super(initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    final void allocateInner(int outerIndex, int innerArrayCapacity) {

    }

    final long addHeadNodeAndReturnNode(INSTANCE instance, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = allocateNextNode();

        setNextNode(node, headNode);

        headNodeSetter.setNode(instance, node);

        if (tailNode == NO_NODE) {

            tailNodeSetter.setNode(instance, node);
        }

        return node;
    }

    final long addTailNodeAndReturnNode(INSTANCE instance, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = allocateNextNode();

        if (tailNode != NO_NODE) {

            setNextNode(tailNode, node);
        }

        tailNodeSetter.setNode(instance, node);

        if (headNode == NO_NODE) {

            setNextNode(node, headNode);

            headNodeSetter.setNode(instance, node);
        }

        setNextNode(node, NO_NODE);

        return node;
    }

    final long removeHeadNodeAndReturnNode(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        if (headNode == NO_NODE) {

            throw new IllegalStateException();
        }

        final long newHeadNode = getNextNode(headNode);

        headNodeSetter.setNode(instance, newHeadNode);

        addNodeToFreeList(headNode);

        if (newHeadNode == NO_NODE) {

            tailNodeSetter.setNode(instance, NO_NODE);
        }

        return headNode;
    }

    final long removeTailNodeAndReturnNode(INSTANCE instance, long newTailNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        if (tailNode == NO_NODE) {

            throw new IllegalStateException();
        }

        addNodeToFreeList(tailNode);

        if (newTailNode != NO_NODE) {

            setNextNode(newTailNode, NO_NODE);

            tailNodeSetter.setNode(instance, newTailNode);
        }
        else {
            headNodeSetter.setNode(instance, NO_NODE);
            tailNodeSetter.setNode(instance, NO_NODE);
        }

        return tailNode;
    }

    final void removeNodeByFindingPreviousNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        long previousNode = NO_NODE;

        for (long node = headNode; node != toRemove; node = getNextNode(node)) {

            previousNode = node;
        }

        removeNode(instance, toRemove, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    final void removeNode(INSTANCE instance, long toRemove, long previousNode, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        if (toRemove == headNode) {

            removeHeadNodeAndReturnNode(instance, headNode, headNodeSetter, tailNodeSetter);
        }
        else if (toRemove == tailNode) {

            removeTailNodeAndReturnNode(instance, previousNode, tailNode, headNodeSetter, tailNodeSetter);
        }
        else {
            if (previousNode != NO_NODE) {

                final long nextNode = getNextNode(toRemove);

                setNextNode(previousNode, nextNode);
            }

            addNodeToFreeList(toRemove);
        }
    }
}
