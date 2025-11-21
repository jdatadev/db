package dev.jdata.db.utils.adt.lists;

abstract class BaseMutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE, VALUES extends BaseIntInnerOuterNodeListValues>

        extends BaseIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE, VALUES>
        implements IMutableIntLargeDoublyLinkedMultiHeadNodeList<INSTANCE> {

    BaseMutableIntLargeDoublyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<int[], IIntInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long addHead(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadAddHeadParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final long addTail(INSTANCE instance, int value, long headNode, long tailNode,ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadAddTailParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final void removeNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveNodeParameters(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        removeListNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final int removeTailAndReturnValue(INSTANCE instance, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveTailAndReturnValueParameters(instance, tailNode, headNodeSetter, tailNodeSetter);

        final int result = getValue(tailNode);

        removeTailNodeAndReturnValue(instance, tailNode, headNodeSetter, tailNodeSetter);

        return result;
    }

    @Override
    public final int removeNodeAndReturnValue(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveNodeAndReturnValueParameters(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        final int result = getValue(toRemove);

        removeListNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return result;
    }

    @Override
    public final long removeAtMostOneNodeByValue(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveAtMostOneNodeByValueParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return removeAtMostOneNodeByValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final void clear(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadClearParameters(instance, headNode, headNodeSetter, tailNodeSetter);

        clearNodes(instance, headNode, headNodeSetter, tailNodeSetter);
    }
}
