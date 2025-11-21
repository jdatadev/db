package dev.jdata.db.utils.adt.lists;

abstract class BaseMutableLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE, VALUES extends BaseLongInnerOuterNodeListValues>

        extends BaseLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE, VALUES>
        implements IMutableLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE> {

    BaseMutableLongLargeDoublyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long addHead(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadAddHeadParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final long addTail(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadAddTailParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final long removeTailAndReturnValue(INSTANCE instance, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveTailAndReturnValueParameters(instance, tailNode, headNodeSetter, tailNodeSetter);

        final long result = getValue(tailNode);

        removeTailNode(instance, tailNode, headNodeSetter, tailNodeSetter);

        return result;
    }

    @Override
    public final void removeNode(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveNodeAndReturnValueParameters(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        removeListNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final long removeNodeAndReturnValue(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveNodeAndReturnValueParameters(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        final long result = getValue(toRemove);

        removeListNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return result;
    }

    @Override
    public final void clear(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadClearParameters(instance, headNode, headNodeSetter, tailNodeSetter);

        clearNodes(instance, headNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    final void clearNumElements() {

    }
}
