package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;

abstract class BaseMutableObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, T, VALUES extends BaseObjectInnerOuterNodeListValues<T>>

        extends BaseObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, T, VALUES>
        implements IMutableObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, T> {

    BaseMutableObjectLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<T[], IObjectInnerOuterNodeListInternal<T>, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long addHead(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadAddHeadParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return addHeadValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final long addTail(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadAddTailParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return addTailValue(instance, value, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final void removeNodeByFindingPrevious(INSTANCE instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveNodeByFindingPreviousParameters(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);

        removeNodeByFindingPreviousNode(instance, toRemove, headNode, tailNode, headNodeSetter, tailNodeSetter);
    }

    @Override
    public final long removeAtMostOneNodeByValue(INSTANCE instance, T value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadRemoveAtMostOneNodeByValueParameters(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        final long noNode = NO_NODE;

        long previousNode = noNode;
        long removedNode = noNode;

        for (long node = headNode; node != noNode; node = getNextNode(node)) {

            if (getValue(node) == value) {

                if (removedNode != noNode) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                removeNodeWithPreviousNode(instance, node, previousNode, headNode, tailNode, headNodeSetter, tailNodeSetter);

                removedNode = node;
            }

            previousNode = node;
        }

        return removedNode;
    }

    @Override
    public void clear(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        checkMultiHeadClearParameters(instance, headNode, headNodeSetter, tailNodeSetter);

        clear(instance, headNode, headNodeSetter, tailNodeSetter);
    }
}
