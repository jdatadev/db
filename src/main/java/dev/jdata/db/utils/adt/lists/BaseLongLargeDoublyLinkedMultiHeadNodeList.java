package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;

abstract class BaseLongLargeDoublyLinkedMultiHeadNodeList<INSTANCE, VALUES extends BaseLongInnerOuterNodeListValues>

        extends BaseLongLargeDoublyLinkedNodeList<INSTANCE, VALUES>
        implements ILongMultiHeadNodeListCommon {

    BaseLongLargeDoublyLinkedMultiHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);
    }

    @Override
    public final boolean contains(long value, long headNode) {

        checkMultiHeadContainsParameters(headNode);

        return containsValue(value, headNode);
    }

    @Override
    public final <P> boolean contains(long headNode, P parameter, ILongElementPredicate<P> predicate) {

        checkMultiHeadContainsParameters(headNode, predicate);

        return containsValue(headNode, parameter, predicate);
    }

    @Override
    public final boolean containsOnly(long value, long headNode) {

        checkMultiHeadContainsOnlyParameters(headNode);

        return containsOnlyValue(value, headNode);
    }

    @Override
    public final boolean containsOnly(long value, long headNode, ILongContainsOnlyPredicate predicate) {

        checkMultiHeadContainsOnlyParameters(headNode, predicate);

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public final long findAtMostOneNode(long value, long headNode) {

        checkMultiHeadFindAtMostOneNodeParameters(headNode);

        return findAtMostOneValueNode(value, headNode);
    }

    @Override
    public final <P> long findAtMostOneValue(long defaultValue, long headNode, P parameter, ILongElementPredicate<P> predicate) {

        checkMultiHeadFindAtMostOneValueParameters(headNode, predicate);

        return findAtMostOneNodeValue(defaultValue, headNode, parameter, predicate);
    }

    @Override
    public final long[] toArray(long headNode) {

        checkMultiHeadToArrayParameters(headNode);

        return toListArrayValues(headNode);
    }
}
