package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IObjectContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IObjectElementPredicate;

abstract class BaseObjectLargeSinglyLinkedMultiHeadNodeList<

                INSTANCE,
                T,
                VALUES extends BaseObjectInnerOuterNodeListValues<T>>

        extends BaseObjectLargeSinglyLinkedNodeList<INSTANCE, T, VALUES>
        implements IObjectSinglyLinkedMultiHeadNodeListCommon<T> {

    BaseObjectLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<T[], IObjectInnerOuterNodeListInternal<T>, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final boolean contains(T value, long headNode) {

        Objects.requireNonNull(value);
        checkMultiHeadContainsParameters(headNode);

        return containsValue(value, headNode);
    }

    @Override
    public final <P> boolean contains(long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        checkMultiHeadContainsParameters(headNode, predicate);

        return containsValue(headNode, parameter, predicate);
    }

    @Override
    public final boolean containsOnly(T value, long headNode) {

        Objects.requireNonNull(value);
        checkMultiHeadContainsOnlyParameters(headNode);

        return containsOnlyValue(value, headNode);
    }

    @Override
    public final boolean containsOnly(T value, long headNode, IObjectContainsOnlyPredicate<T> predicate) {

        Objects.requireNonNull(value);
        checkMultiHeadContainsOnlyParameters(headNode, predicate);

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public final long findAtMostOneNode(T value, long headNode) {

        return findAtMostOneValueNode(value, headNode);
    }

    @Override
    public final <P> T findAtMostOneValue(T defaultValue, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return findAtMostOneNodeValue(defaultValue, headNode, parameter, predicate);
    }

    @Override
    public final T[] toArray(long headNode) {

        checkMultiHeadToArrayParameters(headNode);

        return toListArrayValues(headNode);
    }

    @Override
    final void clearNumElements() {

    }
}
