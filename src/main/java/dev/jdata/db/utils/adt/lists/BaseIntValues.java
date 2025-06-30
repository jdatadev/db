package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IIntElements.IContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IIntElements.IIntElementPredicate;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseIntValues<LIST extends BaseInnerOuterList<int[], LIST, U>, U extends BaseValues<int[], LIST, U>> extends BaseValues<int[], LIST, U> {

    private static final long NO_NODE = BaseInnerOuterList.NO_NODE;

    private int[][] values;

    protected BaseIntValues(int initialOuterCapacity) {

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

        Checks.isIndex(outerIndex);
        Checks.isCapacity(innerCapacity);

        values[outerIndex] = new int[innerCapacity];
    }

    final int getValue(BaseInnerOuterList<int[], LIST, U> list, long node) {

        return values[list.getOuterIndex(node)][list.getInnerIndex(node)];
    }

    final void setValue(BaseInnerOuterList<int[], LIST, U> list, long node, int value) {

        values[list.getOuterIndex(node)][list.getInnerIndex(node)] = value;
    }

    final boolean containsValue(BaseInnerOuterList<int[], LIST, U> list, int value, long headNode) {

        return findValueNode(list, value, headNode) != NO_NODE;
    }

    final <P> boolean containsValue(BaseInnerOuterList<int[], LIST, U> list, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        return findValueNode(list, headNode, parameter, predicate) != NO_NODE;
    }

    final boolean containsOnlyValue(BaseInnerOuterList<int[], LIST, U> list, int value, long headNode) {

        return containsOnlyValue(list, value, headNode, (i, l) -> i == l);
    }

    final boolean containsOnlyValue(BaseInnerOuterList<int[], LIST, U> list, int value, long headNode, IContainsOnlyPredicate containsOnlyPredicate) {

        boolean containsOnly = false;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

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

    final long findValueNode(BaseInnerOuterList<int[], LIST, U> list, int value, long headNode) {

        long found = NO_NODE;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            if (getValue(list, node) == value) {

                found = node;
                break;
            }
        }

        return found;
    }

    final <P> long findValueNode(BaseInnerOuterList<int[], LIST, U> list, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        long found = NO_NODE;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            final int value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                found = node;
                break;
            }
        }

        return found;
    }

    final <P> int findNodeValue(BaseInnerOuterList<int[], LIST, U> list, int defaultValue, long headNode, P parameter, IIntElementPredicate<P> predicate) {

        int found = defaultValue;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            final int value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                found = value;
                break;
            }
        }

        return found;
    }

    @Override
    protected final int[] toArray(LIST list, long headNode, int numElements) {

        Objects.requireNonNull(list);
        BaseList.checkIsNode(headNode);
        Checks.isNumElements(numElements);

        final int[] result = new int[numElements];

        int dstIndex = 0;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            result[dstIndex ++] = getValue(list, node);
        }

        return result;
    }
}
