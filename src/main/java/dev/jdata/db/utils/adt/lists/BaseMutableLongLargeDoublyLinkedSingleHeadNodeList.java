package dev.jdata.db.utils.adt.lists;

abstract class BaseMutableLongLargeDoublyLinkedSingleHeadNodeList<VALUES extends BaseLongInnerOuterNodeListValues>

        extends BaseLongLargeDoublyLinkedSingleHeadNodeList<VALUES>
        implements IMutableLongLargeDoublyLinkedNodeList {

    BaseMutableLongLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long addHeadAndReturnNode(long value) {

        final long result = addHeadValue(this, value, getHeadNode(), getTailNode(), BaseLongLargeDoublyLinkedSingleHeadNodeList::setHeadNode,
                BaseLongLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final long addTailAndReturnNode(long value) {

        final long result = addTailValue(this, value, getHeadNode(), getTailNode(), BaseLongLargeDoublyLinkedSingleHeadNodeList::setHeadNode,
                BaseLongLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final long removeHeadAndReturnValue() {

        final long headNode = getHeadNode();

        final long result = getValue(headNode);

        removeHeadNode(headNode);

        return result;
    }

    @Override
    public final long removeTailAndReturnValue() {

        final long tailNode = getTailNode();

        final long result = getValue(tailNode);

        removeTailNode(tailNode);

        return result;
    }

    @Override
    public final long removeNodeAndReturnValue(long toRemove) {

        checkSingleHeadRemoveNodeParameters(toRemove);

        final long result = getValue(toRemove);

        removeNodeListNode(toRemove);

        return result;
    }

    private void removeHeadNode(long headNode) {

        removeHeadNode(this, headNode, getTailNode(), BaseLongLargeDoublyLinkedSingleHeadNodeList::setHeadNode, BaseLongLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }

    private void removeTailNode(long tailNode) {

        removeTailNode(this, tailNode, BaseLongLargeDoublyLinkedSingleHeadNodeList::setHeadNode, BaseLongLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }

    private void removeNodeListNode(long toRemove) {

        removeListNode(this, toRemove, getHeadNode(), getTailNode(), BaseLongLargeDoublyLinkedSingleHeadNodeList::setHeadNode,
                BaseLongLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }
}
