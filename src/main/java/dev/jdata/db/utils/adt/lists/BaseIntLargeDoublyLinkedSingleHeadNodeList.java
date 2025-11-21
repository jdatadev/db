package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IIntElementPredicate;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.adt.elements.IIntForEachWithResult;

abstract class BaseIntLargeDoublyLinkedSingleHeadNodeList<VALUES extends BaseIntInnerOuterNodeListValues>

        extends BaseIntLargeDoublyLinkedNodeList<BaseIntLargeDoublyLinkedSingleHeadNodeList<VALUES>, VALUES>
        implements IIntDoublyLinkedSingleHeadNodeListCommon {

    private long headNode;
    private long tailNode;

    private long numElements;

    BaseIntLargeDoublyLinkedSingleHeadNodeList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity,
            ILargeNodeListValuesFactory<int[], IIntInnerOuterNodeListInternal, VALUES> valuesFactory) {
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
    public final boolean contains(int value) {

        return containsValue(value, headNode);
    }

    @Override
    public final boolean containsOnly(int value) {

        return containsOnlyValue(value, headNode);
    }

    @Override
    public final <P> boolean contains(P parameter, IIntElementPredicate<P> predicate) {

        checkSingleHeadContainsParameters(predicate);

        return containsValue(headNode, parameter, predicate);
    }

    @Override
    public final boolean containsOnly(int value, IIntContainsOnlyPredicate predicate) {

        checkSingleHeadContainsOnlyParameters(predicate);

        return containsOnlyValue(value, headNode, predicate);
    }

    @Override
    public final long findAtMostOneNode(int value) {

        return findAtMostOneValueNode(value, headNode);
    }

    @Override
    public final <P> int findAtMostOneValue(int defaultValue, P parameter, IIntElementPredicate<P> predicate) {

        checkSingleHeadFindAtMostOneValueParameters(predicate);

        return findAtMostOneNodeValue(defaultValue, defaultValue, parameter, predicate);
    }

    @Override
    public final int[] toArray() {

        return toListArrayValues(headNode, Array.intCapacity(numElements));
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long noNode = NO_NODE;

        for (long node = headNode; node != noNode; node = getNextNode(node)) {

            forEach.each(getValue(node), parameter);
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E {

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
