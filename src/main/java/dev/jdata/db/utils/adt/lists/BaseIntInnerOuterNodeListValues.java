package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.IIntContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IIntElementPredicate;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseIntInnerOuterNodeListValues extends BaseInnerOuterNodeListValues<int[], IIntInnerOuterNodeListInternal> implements IIntNodeListValuesMarker {

    private int[][] values;

    BaseIntInnerOuterNodeListValues(int initialOuterCapacity) {

        this.values = new int[initialOuterCapacity][];
    }

    @Override
    protected final void reallocateOuter(int newOuterLength) {

        if (newOuterLength <= values.length) {

            throw new IllegalArgumentException();
        }

        this.values = Arrays.copyOf(values, newOuterLength);
    }

    @Override
    protected final void allocateInner(int outerIndex, int innerCapacity) {

        Checks.isOuterIndex(outerIndex);
        Checks.isInnerCapacity(innerCapacity);

        values[outerIndex] = new int[innerCapacity];
    }

    final int getValue(IIntInnerOuterNodeListInternal list, long node) {

        return values[list.getOuterIndex(node)][list.getInnerIndex(node)];
    }

    final void setValue(IIntInnerOuterNodeListInternal list, long node, int value) {

        values[list.getOuterIndex(node)][list.getInnerIndex(node)] = value;
    }

    final boolean containsValue(IIntInnerOuterNodeListInternal list, int value, long headNode) {

        return findValueNode(list, value, headNode, false) != NO_NODE;
    }

    final <P> boolean containsValue(IIntInnerOuterNodeListInternal list, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        return findValueNode(list, headNode, false, parameter, predicate) != NO_NODE;
    }

    final boolean containsOnlyValue(IIntInnerOuterNodeListInternal list, int value, long headNode) {

        return containsOnlyValue(list, value, headNode, (i, l) -> i == l);
    }

    final boolean containsOnlyValue(IIntInnerOuterNodeListInternal list, int value, long headNode, IIntContainsOnlyPredicate containsOnlyPredicate) {

        boolean containsOnly = false;

        final long noNode = NO_NODE;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            if (containsOnlyPredicate.test(value, getValue(list, node))) {

                containsOnly = true;
            }
            else {
                containsOnly = false;
                break;
            }
        }

        return containsOnly;
    }

    final long findAtMostOneValueNode(IIntInnerOuterNodeListInternal list, int value, long headNode) {

        return findValueNode(list, value, headNode, true);
    }

    private long findValueNode(IIntInnerOuterNodeListInternal list, int value, long headNode, boolean atMostOne) {

        final long noNode = NO_NODE;

        long found = noNode;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            if (getValue(list, node) == value) {

                if (atMostOne) {

                    if (found != noNode) {

                        throw ElementsExceptions.moreThanOneFoundException();
                    }

                    found = node;
                }
                else {
                    found = node;
                    break;
                }
            }
        }

        return found;
    }

    final <P> long findAtMostOneValueNode(IIntInnerOuterNodeListInternal list, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        return findValueNode(list, headNode, true, parameter, predicate);
    }

    private <P> long findValueNode(IIntInnerOuterNodeListInternal list, long headNode, boolean atMostOne, P parameter, IIntElementPredicate<P> predicate) {

        final long noNode = NO_NODE;

        long found = noNode;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            final int value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                if (atMostOne) {

                    if (found != noNode) {

                        throw ElementsExceptions.moreThanOneFoundException();
                    }

                    found = node;
                }
                else {
                    found = node;
                    break;
                }
            }
        }

        return found;
    }

    final <P> int findAtMostOneNodeValue(IIntInnerOuterNodeListInternal list, int defaultValue, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        final long noNode = NO_NODE;

        long foundNode = noNode;
        int found = defaultValue;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            final int value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                if (foundNode != noNode) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                found = value;
                foundNode = value;
            }
        }

        return found;
    }

    @Override
    protected final int[] toArray(IIntInnerOuterNodeListInternal list, long headNode, int numElements) {

        Objects.requireNonNull(list);
        BaseNodeList.checkHeadNode(headNode);
        Checks.isIntNumElements(numElements);

        final int[] result = new int[numElements];

        final long noNode = NO_NODE;

        int dstIndex = 0;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            result[dstIndex ++] = getValue(list, node);
        }

        return result;
    }
}
