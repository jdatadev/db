package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IIntElementPredicate;

abstract class BaseIntLargeSinglyLinkedMultiHeadNodeList<INSTANCE, VALUES extends BaseIntInnerOuterNodeListValues>

        extends BaseIntLargeSinglyLinkedNodeList<INSTANCE, VALUES>
        implements IIntSinglyLinkedMultiHeadNodeListCommon {

    BaseIntLargeSinglyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<int[], IIntInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final boolean contains(int value, long headNode) {

        checkMultiHeadContainsParameters(headNode);

        return containsValue(value, headNode);
    }

    @Override
    public final <P> boolean contains(long headNode, P parameter, IIntElementPredicate<P> predicate) {

        checkMultiHeadContainsParameters(headNode, predicate);

        return containsValue(headNode, parameter, predicate);
    }

    @Override
    public final boolean containsOnly(int value, long headNode) {

        checkMultiHeadContainsOnlyParameters(headNode);

        return containsOnly(value, headNode);
    }

    @Override
    public final boolean containsOnly(int value, long headNode, IIntContainsOnlyPredicate predicate) {

        checkMultiHeadContainsOnlyParameters(headNode, predicate);

        return containsOnly(value, headNode, predicate);
    }

    @Override
    public final long findAtMostOneNode(int value, long headNode) {

        checkMultiHeadFindAtMostOneNodeParameters(headNode);

        return findAtMostOneValueNode(value, headNode);
    }

    @Override
    public final <P> int findAtMostOneValue(int defaultValue, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        checkMultiHeadFindAtMostOneValueParameters(headNode, predicate);

        return findAtMostOneNodeValue(defaultValue, headNode, parameter, predicate);
    }

    @Override
    public final int[] toArray(long headNode) {

        checkMultiHeadToArrayParameters(headNode);

        return toListArrayValues(headNode);
    }
}
