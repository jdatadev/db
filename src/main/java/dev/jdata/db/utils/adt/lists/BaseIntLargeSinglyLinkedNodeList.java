package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IIntElementPredicate;

abstract class BaseIntLargeSinglyLinkedNodeList<INSTANCE, VALUES extends BaseIntInnerOuterNodeListValues>

        extends BaseLargeSinglyLinkedNodeList<INSTANCE, int[], IIntInnerOuterNodeListInternal, VALUES>
        implements IIntInnerOuterNodeListInternal {

    BaseIntLargeSinglyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<int[], IIntInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final int getValue(long node) {

        return getValues().getValue(this, node);
    }

    final boolean containsValue(int value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final <P> boolean containsValue(long headNode, P parameter, IIntElementPredicate<P> predicate) {

        return getValues().containsValue(this, headNode, parameter, predicate);
    }

    final boolean containsOnlyValue(int value, long headNode) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    final boolean containsOnlyValue(int value, long headNode, IIntContainsOnlyPredicate containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findAtMostOneValueNode(int value, long headNode) {

        return getValues().findAtMostOneValueNode(this, value, headNode);
    }

    final <P> int findAtMostOneNodeValue(int defaultValue, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        return getValues().findAtMostOneNodeValue(this, defaultValue, headNode, parameter, predicate);
    }

    final long addHeadValue(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(INSTANCE instance, int value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final int removeHeadAndReturnValue(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final int result = getValue(headNode);

        removeHeadNode(instance, headNode, headNodeSetter, tailNodeSetter);

        return result;
    }

    private void setValue(long node, int value) {

        getValues().setValue(this, node, value);
    }
}
