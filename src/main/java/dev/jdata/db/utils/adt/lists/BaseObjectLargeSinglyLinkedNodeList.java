package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IObjectElementPredicate;

abstract class BaseObjectLargeSinglyLinkedNodeList<

                INSTANCE,
                T,
                VALUES extends BaseObjectInnerOuterNodeListValues<T>>

        extends BaseLargeSinglyLinkedNodeList<INSTANCE, T[], IObjectInnerOuterNodeListInternal<T>, VALUES>
        implements IObjectInnerOuterNodeListInternal<T> {

    BaseObjectLargeSinglyLinkedNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<T[], IObjectInnerOuterNodeListInternal<T>, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    public final T getValue(long node) {

        return getValues().getValue(this, node);
    }

    final boolean containsValue(T value, long headNode) {

        return getValues().containsValue(this, value, headNode);
    }

    final <P> boolean containsValue(long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return getValues().containsValue(this, headNode, parameter, predicate);
    }

    final boolean containsOnlyValue(T value, long headNode) {

        return getValues().containsOnlyValue(this, value, headNode);
    }

    final boolean containsOnlyValue(T value, long headNode, IObjectContainsOnlyPredicate<T> containsOnlyPredicate) {

        return getValues().containsOnlyValue(this, value, headNode, containsOnlyPredicate);
    }

    final long findAtMostOneValueNode(T value, long headNode) {

        return getValues().findAtMostOneValueNode(this, value, headNode);
    }

    final <P> T findAtMostOneNodeValue(T defaultValue, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return getValues().findAtMostOneNodeValue(this, defaultValue, headNode, parameter, predicate);
    }

    final long addHeadValue(INSTANCE instance, T value, long headNode, long tailNode,
            ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addHeadNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final long addTailValue(INSTANCE instance, T value, long headNode, long tailNode,
            ILongNodeSetter<INSTANCE> headNodeSetter, ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final long node = addTailNodeAndReturnNode(instance, headNode, tailNode, headNodeSetter, tailNodeSetter);

        setValue(node, value);

        return node;
    }

    final T removeHeadAndReturnValue(INSTANCE instance, long headNode, ILongNodeSetter<INSTANCE> headNodeSetter,
            ILongNodeSetter<INSTANCE> tailNodeSetter) {

        final T result = getValue(headNode);

        removeHeadNode(instance, headNode, headNodeSetter, tailNodeSetter);

        return result;
    }

    private void setValue(long node, T value) {

        getValues().setValue(this, node, value);
    }
}
