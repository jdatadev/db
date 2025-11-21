package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;

abstract class BaseMutableLongLargeSinglyLinkedSingleHeadNodeList<VALUES extends BaseLongInnerOuterNodeListValues>

        extends BaseLongLargeSinglyLinkedSingleHeadNodeList<VALUES>
        implements IMutableLongLargeSinglyLinkedNodeList {

    BaseMutableLongLargeSinglyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long addHeadAndReturnNode(long value) {

        final long result = addHeadValue(this, value, getHeadNode(), getTailNode(), BaseLongLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseLongLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final long addTailAndReturnNode(long value) {

        final long result = addTailValue(this, value, getHeadNode(), getTailNode(), BaseLongLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseLongLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final long removeHeadAndReturnValue() {

        final long result = removeHeadAndReturnValue(this, getHeadNode(), BaseLongLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseLongLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();

        return result;
    }

    @Override
    public final boolean removeAtMostOne(long element) {

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

                removeNodeWithPreviousNode(this, removedNode, previousNode, headNode, getTailNode(),
                        BaseLongLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                        BaseLongLargeSinglyLinkedSingleHeadNodeList::setTailNode);

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

        removeNodeByFindingPreviousNode(this, toRemove, getHeadNode(), getTailNode(), BaseLongLargeSinglyLinkedSingleHeadNodeList::setHeadNode,
                BaseLongLargeSinglyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }
}
