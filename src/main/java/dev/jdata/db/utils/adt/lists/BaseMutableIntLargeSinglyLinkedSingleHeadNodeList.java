package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;

abstract class BaseMutableIntLargeSinglyLinkedSingleHeadNodeList<VALUES extends BaseIntInnerOuterNodeListValues>

        extends BaseIntLargeSinglyLinkedSingleHeadNodeList<VALUES>
        implements IBaseMutableIntSinglyLinkedNodeList {

    BaseMutableIntLargeSinglyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<int[], IIntInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long addHeadAndReturnNode(int value) {

        final long result = addHeadValue(this, value, getHeadNode(), getTailNode(), BaseIntLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseIntLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final long addTailAndReturnNode(int value) {

        final long result = addTailValue(this, value, getHeadNode(), getTailNode(), BaseIntLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseIntLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final int removeHeadAndReturnValue() {

        final int result = removeHeadAndReturnValue(this, getHeadNode(), BaseIntLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseIntLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();

        return result;
    }

    @Override
    public final boolean removeAtMostOneElement(int element) {

        boolean result = false;

        final long noNode = NO_NODE;

        long previousNode = noNode;
        long removedNode = noNode;

        final long headNode = getHeadNode();

        for (long node = headNode; node != noNode; node = getNextNode(node)) {

            if (getValue(node) == element) {

                if (removedNode != noNode) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                removeNodeWithPreviousNode(this, removedNode, previousNode, headNode, getTailNode(), BaseIntLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                        BaseIntLargeSinglyLinkedSingleHeadNodeList::setTailNode);

                removedNode = node;
                break;
            }

            previousNode = node;
        }

        return result;
    }

    @Override
    public final void removeNodeByFindingPrevious(long toRemove) {

        checkSingleHeadRemoveNodeByFindingPreviousParameters(toRemove);

        removeNodeByFindingPreviousNode(this, toRemove, getHeadNode(), getTailNode(), BaseIntLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseIntLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }
}
