package dev.jdata.db.utils.adt.lists;

abstract class BaseMutableIntLargeDoublyLinkedSingleHeadNodeList<VALUES extends BaseIntInnerOuterNodeListValues>

        extends BaseIntLargeDoublyLinkedSingleHeadNodeList<VALUES>
        implements IMutableIntLargeDoublyLinkedNodeList {

    BaseMutableIntLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<int[], IIntInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long addHeadAndReturnNode(int value) {

        final long result = addHeadValue(this, value, getHeadNode(), getTailNode(), BaseIntLargeDoublyLinkedSingleHeadNodeList::setHeadNode,
                BaseIntLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final long addTailAndReturnNode(int value) {

        final long result = addTailValue(this, value, getHeadNode(), getTailNode(), BaseIntLargeDoublyLinkedSingleHeadNodeList::setHeadNode,
                BaseIntLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        increaseNumElements();

        return result;
    }

    @Override
    public final int removeHeadAndReturnValue() {

        final long headNode = getHeadNode();

        final int result = getValue(headNode);

        removeHeadNode(headNode);

        return result;
    }

    @Override
    public final int removeTailAndReturnValue() {

        final long tailNode = getTailNode();

        final int result = getValue(tailNode);

        removeTailNode(tailNode);

        return result;
    }

    @Override
    public final int removeNodeAndReturnValue(long toRemove) {

        checkSingleHeadRemoveNodeParameters(toRemove);

        final int result = getValue(toRemove);

        removeNodeListNode(toRemove);

        return result;
    }

    private void removeHeadNode(long headNode) {

        removeHeadNode(this, headNode, getTailNode(), BaseIntLargeDoublyLinkedSingleHeadNodeList::setHeadNode, BaseIntLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }

    private void removeTailNode(long tailNode) {

        removeTailNode(this, tailNode, BaseIntLargeDoublyLinkedSingleHeadNodeList::setHeadNode, BaseIntLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }

    private void removeNodeListNode(long toRemove) {

        removeListNode(this, toRemove, getHeadNode(), getTailNode(), BaseIntLargeDoublyLinkedSingleHeadNodeList::setHeadNode,
                BaseIntLargeDoublyLinkedSingleHeadNodeList::setTailNode);

        decreaseNumElements();
    }
}
