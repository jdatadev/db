package dev.jdata.db.utils.adt.lists;

abstract class BaseLargeSinglyLinkedNodeList<

                INSTANCE,
                TO_ARRAY,
                VALUES_LIST extends IInnerOuterNodeListInternal<TO_ARRAY>,
                VALUES extends BaseInnerOuterNodeListValues<TO_ARRAY, VALUES_LIST>>

        extends BaseLargeNodeList<TO_ARRAY, VALUES_LIST, VALUES> {

    BaseLargeSinglyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<TO_ARRAY, VALUES_LIST, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    final void reallocateOuter(int newOuterLength) {

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

        final long noNode = NO_NODE;

        if (tailNode != noNode) {

            setNextNode(tailNode, node);
        }

        tailNodeSetter.setNode(instance, node);

        if (headNode == noNode) {

            setNextNode(node, headNode);

            headNodeSetter.setNode(instance, node);
        }

        setNextNode(node, noNode);

        return node;
    }

    final void removeHeadNode(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long noNode = NO_NODE;

        if (headNode == noNode) {

            throw new IllegalStateException();
        }

        final long newHeadNode = getNextNode(headNode);

        headNodeSetter.setNode(instance, newHeadNode);

        addNodeToFreeList(headNode);

        if (newHeadNode == noNode) {

            tailNodeSetter.setNode(instance, noNode);
        }
    }

    final void removeTailNode(INSTANCE instance, long newTailNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long noNode = NO_NODE;

        if (tailNode == noNode) {

            throw new IllegalStateException();
        }

        addNodeToFreeList(tailNode);

        if (newTailNode != noNode) {

            setNextNode(newTailNode, noNode);

            tailNodeSetter.setNode(instance, newTailNode);
        }
        else {
            headNodeSetter.setNode(instance, noNode);
            tailNodeSetter.setNode(instance, noNode);
        }
    }

    final void removeNodeByFindingPreviousNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        long previousNode = NO_NODE;

        for (long node = headNode; node != toRemove; node = getNextNode(node)) {

            previousNode = node;
        }

        removeNodeWithPreviousNode(instance, toRemove, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    final void removeNodeWithPreviousNode(INSTANCE instance, long toRemove, long previousNode, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        if (isEmpty(headNode, tailNode)) {

            throw new IllegalStateException();
        }

        if (toRemove == headNode) {

            removeHeadNode(instance, headNode, headNodeSetter, tailNodeSetter);
        }
        else if (toRemove == tailNode) {

            removeTailNode(instance, previousNode, tailNode, headNodeSetter, tailNodeSetter);
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
