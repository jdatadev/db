package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.BaseElements;
import dev.jdata.db.utils.adt.elements.IContainsOnlyPredicateMarker;
import dev.jdata.db.utils.adt.elements.IElementPredicateMarker;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseNodeList<TO_ARRAY, VALUES_LIST extends INodeListInternal<TO_ARRAY>, VALUES extends BaseNodeListValues<TO_ARRAY, VALUES_LIST>>

        extends BaseElements<Void, Void, Void>
        implements INodeListInternal<TO_ARRAY> {

    static final long NO_NODE = LargeNodeLists.NO_LONG_NODE;

    static long checkIsHeadNodeSet(long headNode) {

        checkIsValidNode(headNode);

        if (headNode == NO_NODE) {

            throw new IllegalStateException();
        }

        return headNode;
    }

    static long checkIsTailNodeSet(long tailNode) {

        checkIsValidNode(tailNode);

        if (tailNode == NO_NODE) {

            throw new IllegalStateException();
        }

        return tailNode;
    }

    static void checkGetValueParameters(long node) {

        checkIsNode(node);
    }

    static void checkSingleHeadContainsParameters(IElementPredicateMarker predicate) {

        Objects.requireNonNull(predicate);
    }

    static void checkSingleHeadContainsOnlyParameters(IContainsOnlyPredicateMarker predicate) {

        Objects.requireNonNull(predicate);
    }

    static void checkSingleHeadFindAtMostOneValueParameters(IElementPredicateMarker predicate) {

        Objects.requireNonNull(predicate);
    }

    static void checkSingleHeadRemoveNodeParameters(long toRemove) {

        checkRemoveNode(toRemove);
    }

    static void checkSingleHeadRemoveNodeByFindingPreviousParameters(long toRemove) {

        checkRemoveNode(toRemove);
    }

    static void checkMultiHeadContainsParameters(long headNode) {

        checkHeadNode(headNode);
    }

    static void checkMultiHeadContainsParameters(long headNode, IElementPredicateMarker predicate) {

        checkHeadNode(headNode);
        Objects.requireNonNull(predicate);
    }

    static void checkMultiHeadContainsOnlyParameters(long headNode) {

        checkHeadNode(headNode);
    }

    static void checkMultiHeadContainsOnlyParameters(long headNode, IContainsOnlyPredicateMarker predicate) {

        checkHeadNode(headNode);
        Objects.requireNonNull(predicate);
    }

    static void checkMultiHeadFindAtMostOneNodeParameters(long headNode) {

        checkHeadNode(headNode);
    }

    static void checkMultiHeadFindAtMostOneValueParameters(long headNode, IElementPredicateMarker predicate) {

        checkHeadNode(headNode);
        Objects.requireNonNull(predicate);
    }

    static void checkMultiHeadToArrayParameters(long headNode) {

        checkHeadNode(headNode);
    }

    static <T> void checkMultiHeadAddHeadParameters(T instance, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkHeadNode(headNode);
        checkTailNode(tailNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static <T> void checkMultiHeadAddTailParameters(T instance, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkHeadNode(headNode);
        checkTailNode(tailNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static <T> void checkMultiHeadRemoveNodeParameters(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter,
            ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkRemoveNode(toRemove);
        checkHeadNode(headNode);
        checkTailNode(tailNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static <T> void checkMultiHeadRemoveNodeByFindingPreviousParameters(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter,
            ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkRemoveNode(toRemove);
        checkHeadNode(headNode);
        checkTailNode(tailNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static <T> void checkMultiHeadRemoveTailAndReturnValueParameters(T instance, long tailNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkTailNode(tailNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static <T> void checkMultiHeadRemoveNodeAndReturnValueParameters(T instance, long toRemove, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter,
            ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkRemoveNode(toRemove);
        checkHeadNode(headNode);
        checkTailNode(tailNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static <T> void checkMultiHeadRemoveAtMostOneNodeByValueParameters(T instance, long headNode, long tailNode, ILongNodeSetter<T> headNodeSetter,
            ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkHeadNode(headNode);
        checkTailNode(tailNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static <T> void checkMultiHeadClearParameters(T instance, long headNode, ILongNodeSetter<T> headNodeSetter, ILongNodeSetter<T> tailNodeSetter) {

        Objects.requireNonNull(instance);
        checkHeadNode(headNode);
        Objects.requireNonNull(headNodeSetter);
        Objects.requireNonNull(tailNodeSetter);
    }

    static long checkHeadNode(long headNode) {

        checkIsNode(headNode);

        return headNode;
    }

    private static long checkTailNode(long tailNode) {

        checkIsNode(tailNode);

        return tailNode;
    }

    private static long checkRemoveNode(long toRemove) {

        checkIsNode(toRemove);

        return toRemove;
    }

    private static long checkIsNode(long node) {

        checkIsValidNode(node);

        if (node == NO_NODE) {

            throw new IllegalArgumentException();
        }

        return node;
    }

    private static long checkIsValidNode(long node) {

        if (node < 0L) {

            throw new IllegalArgumentException();
        }

        return node;
    }

    private final VALUES values;

    BaseNodeList(AllocationType allocationType, VALUES values) {
        super(allocationType);

        this.values = values;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<Void, Void, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final Void copyValues(Void elements, long startIndex, long numElements) {

        checkIntOrLongCopyValuesParameters(elements, 0L, startIndex, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void initializeWithValues(Void values, long numElements) {

        checkIntOrLongIntitializeWithValuesParameters(values, 0L, numElements);

        throw new UnsupportedOperationException();
    }

    final VALUES getValues() {
        return values;
    }

    final TO_ARRAY toListArrayValues(long headNode) {

        checkIsNode(headNode);

        return toListArrayValues(headNode, Array.intCapacity(getNumElements(headNode)));
    }

    final TO_ARRAY toListArrayValues(long headNode, int numElements) {

        checkIsNode(headNode);
        Checks.isIntNumElements(numElements);

        return values.toArray(getThis(), headNode, numElements);
    }

    long getNumElements(long headNode) {

        long count = 0L;

        for (long node = headNode; node != NO_NODE; node = getNextNode(node)) {

            ++ count;
        }

        return count;
    }

    @SuppressWarnings("unchecked")
    private VALUES_LIST getThis() {

        return (VALUES_LIST)this;
    }
}
