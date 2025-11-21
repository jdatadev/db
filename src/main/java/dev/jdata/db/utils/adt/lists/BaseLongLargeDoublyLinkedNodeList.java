package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;

abstract class BaseLongLargeDoublyLinkedNodeList<INSTANCE, VALUES extends BaseLongInnerOuterNodeListValues>

        extends BaseLargeDoublyLinkedNodeList<INSTANCE, long[], ILongInnerOuterNodeListInternal, VALUES>
        implements ILongNodeListCommon, ILongInnerOuterNodeListInternal {

    BaseLongLargeDoublyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final long getValue(long node) {

        return getValues().getValue(this, node);
    }

    final boolean containsValue(long value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final <P> boolean containsValue(long headNode, P parameter, ILongElementPredicate<P> predicate) {

        return getValues().containsValue(this, headNode, parameter, predicate);
    }

    final boolean containsOnlyValue(long value, long headNode) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    final boolean containsOnlyValue(long value, long headNode, ILongContainsOnlyPredicate containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findAtMostOneValueNode(long value, long headNode) {

        return getValues().findAtMostOneValueNode(this, value, headNode);
    }

    final <P> long findAtMostOneNodeValue(long defaultValue, long headNode, P parameter, ILongElementPredicate<P> predicate) {

        return getValues().findAtMostOneNodeValue(this, defaultValue, headNode, parameter, predicate);
    }

    final long addHeadValue(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(INSTANCE instance, long value, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long removeHeadAndReturnValue(INSTANCE instance, long headNode, long tailNode, ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long result = getValue(headNode);

        removeHeadNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        return result;
    }

    private void setValue(long node, long value) {

        getValues().setValue(this, node, value);
    }
}
