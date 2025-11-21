package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;

abstract class BaseLongLargeDoublyLinkedSingleHeadNodeList<VALUES extends BaseLongInnerOuterNodeListValues>

        extends BaseLongLargeDoublyLinkedNodeList<BaseLongLargeDoublyLinkedSingleHeadNodeList<VALUES>, VALUES>
        implements ILongDoublyLinkedSingleHeadNodeListCommon {

    private long headNode;
    private long tailNode;

    private long numElements;

    BaseLongLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<long[], ILongInnerOuterNodeListInternal, VALUES> valuesFactory) {
        super(allocationType, initialOuterCapacity, innerCapacity, valuesFactory);

        this.headNode = NO_NODE;
        this.tailNode = NO_NODE;

        this.numElements = 0L;
    }

    @Override
    public final boolean isEmpty() {

        return isEmpty(headNode, tailNode);
    }

    @Override
    public final long getNumElements() {
        return numElements;
    }

    @Override
    public final boolean contains(long value) {

        return containsValue(value, headNode);
    }

    @Override
    public final boolean containsOnly(long value) {

        return containsOnlyValue(value, headNode);
    }

    @Override
    public final <P> boolean contains(P parameter, ILongElementPredicate<P> predicate) {

        checkSingleHeadContainsParameters(predicate);

        return containsValue(headNode, parameter, predicate);
    }

    @Override
    public final boolean containsOnly(long value, ILongContainsOnlyPredicate predicate) {

        checkSingleHeadContainsOnlyParameters(predicate);

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public final long findAtMostOneNode(long value) {

        return findAtMostOneValueNode(value, headNode);
    }

    @Override
    public final <P> long findAtMostOneValue(long defaultValue, P parameter, ILongElementPredicate<P> predicate) {

        checkSingleHeadFindAtMostOneValueParameters(predicate);

        return findAtMostOneNodeValue(defaultValue, defaultValue, parameter, predicate);
    }

    @Override
    public final long[] toArray() {

        return toListArrayValues(headNode, Array.intCapacity(numElements));
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long noNode = NO_NODE;

        for (long node = headNode; node != noNode; node = getNextNode(node)) {

            forEach.each(getValue(node), parameter);
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long noNode = NO_NODE;

        for (long node = headNode; node != noNode; node = getNextNode(node)) {

            final R forEachResult = forEach.each(getValue(node), parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    public final long getHeadNode() {

        return checkIsHeadNodeSet(headNode);
    }

    @Override
    public final long getTailNode() {

        return checkIsTailNodeSet(tailNode);
    }

    @Override
    final void clearNumElements() {

        this.headNode = NO_NODE;
        this.tailNode = NO_NODE;

        this.numElements = 0L;
    }

    final void setHeadNode(long headNode) {

        this.headNode = headNode;
    }

    final void setTailNode(long tailNode) {

        this.tailNode = tailNode;
    }

    final void increaseNumElements() {

        ++numElements;
    }

    final void decreaseNumElements() {

        --numElements;
    }
}
