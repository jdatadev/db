package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongInnerOuterNodeListValues extends BaseInnerOuterNodeListValues<long[], ILongInnerOuterNodeListInternal> implements ILongNodeListValuesMarker {

    private long[][] values;

    protected BaseLongInnerOuterNodeListValues(int initialOuterCapacity) {

        this.values = new long[initialOuterCapacity][];
    }

    @Override
    protected void reallocateOuter(int newOuterLength) {

        if (newOuterLength <= values.length) {

            throw new IllegalArgumentException();
        }

        this.values = Arrays.copyOf(values, newOuterLength);
    }

    @Override
    protected void allocateInner(int outerIndex, int innerCapacity) {

        Checks.isOuterIndex(outerIndex);
        Checks.isInnerCapacity(innerCapacity);

        values[outerIndex] = new long[innerCapacity];
    }

    final long getValue(ILongInnerOuterNodeListInternal list, long node) {

        return values[list.getOuterIndex(node)][list.getInnerIndex(node)];
    }

    final void setValue(ILongInnerOuterNodeListInternal list, long node, long value) {

        values[list.getOuterIndex(node)][list.getInnerIndex(node)] = value;
    }

    final boolean containsValue(ILongInnerOuterNodeListInternal list, long value, long headNode) {

        return findAtMostOneValueNode(list, value, headNode) != NO_NODE;
    }

    final <P> boolean containsValue(ILongInnerOuterNodeListInternal list, long headNode, P parameter, ILongElementPredicate<P> predicate) {

        return findValueNode(list, headNode, false, parameter, predicate) != NO_NODE;
    }

    final boolean containsOnlyValue(ILongInnerOuterNodeListInternal list, long value, long headNode) {

        return containsOnlyValue(list, value, headNode, (i, l) -> i == l);
    }

    final boolean containsOnlyValue(ILongInnerOuterNodeListInternal list, long value, long headNode, ILongContainsOnlyPredicate containsOnlyPredicate) {

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

    final long findAtMostOneValueNode(ILongInnerOuterNodeListInternal list, long value, long headNode) {

        return findValueNode(list, value, headNode, true);
    }

    private long findValueNode(ILongInnerOuterNodeListInternal list, long value, long headNode, boolean atMostOne) {

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

    final <P> long findAtMostOneValueNode(ILongInnerOuterNodeListInternal list, long headNode, P parameter, ILongElementPredicate<P> predicate) {

        return findValueNode(list, headNode, true, parameter, predicate);
    }

    private <P> long findValueNode(ILongInnerOuterNodeListInternal list, long headNode, boolean atMostOne, P parameter, ILongElementPredicate<P> predicate) {

        final long noNode = NO_NODE;

        long found = noNode;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            final long value = getValue(list, node);

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

    final <P> long findAtMostOneNodeValue(ILongInnerOuterNodeListInternal list, long defaultValue, long headNode, P parameter, ILongElementPredicate<P> predicate) {

        final long noNode = NO_NODE;

        long foundNode = noNode;
        long found = defaultValue;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            final long value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                if (foundNode != noNode) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                found = value;
                foundNode = node;
            }
        }

        return found;
    }

    @Override
    protected final long[] toArray(ILongInnerOuterNodeListInternal list, long headNode, int numElements) {

        Objects.requireNonNull(list);
        BaseNodeList.checkHeadNode(headNode);
        Checks.isIntNumElements(numElements);

        final long[] result = new long[numElements];

        final long noNode = NO_NODE;

        int dstIndex = 0;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            result[dstIndex ++] = getValue(list, node);
        }

        return result;
    }
}
