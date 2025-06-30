package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.elements.ILongElements.IContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElements.LongElementPredicate;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseLongValues<LIST extends BaseInnerOuterList<long[], LIST, U>, U extends BaseValues<long[], LIST, U>> extends BaseValues<long[], LIST, U> {

    private static final long NO_NODE = BaseInnerOuterList.NO_NODE;

    private long[][] values;

    protected BaseLongValues(int initialOuterCapacity) {

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

        Checks.isIndex(outerIndex);
        Checks.isCapacity(innerCapacity);

        values[outerIndex] = new long[innerCapacity];
    }

    final long getValue(BaseInnerOuterList<long[], LIST, U> list, long node) {

        return values[list.getOuterIndex(node)][list.getInnerIndex(node)];
    }

    final void setValue(BaseInnerOuterList<long[], LIST, U> list, long node, long value) {

        values[list.getOuterIndex(node)][list.getInnerIndex(node)] = value;
    }

    final boolean containsValue(BaseInnerOuterList<long[], LIST, U> list, long value, long headNode) {

        return findValueNode(list, value, headNode) != NO_NODE;
    }

    final <P> boolean containsValue(BaseInnerOuterList<long[], LIST, U> list, long headNode, P parameter, LongElementPredicate<P> predicate) {

        return findValueNode(list, headNode, parameter, predicate) != NO_NODE;
    }

    final boolean containsOnlyValue(BaseInnerOuterList<long[], LIST, U> list, long value, long headNode) {

        return containsOnlyValue(list, value, headNode, (i, l) -> i == l);
    }

    final boolean containsOnlyValue(BaseInnerOuterList<long[], LIST, U> list, long value, long headNode, IContainsOnlyPredicate containsOnlyPredicate) {

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

    final long findValueNode(BaseInnerOuterList<long[], LIST, U> list, long value, long headNode) {

        long found = NO_NODE;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            if (getValue(list, node) == value) {

                found = node;
                break;
            }
        }

        return found;
    }

    final <P> long findValueNode(BaseInnerOuterList<long[], LIST, U> list, long headNode, P parameter, LongElementPredicate<P> predicate) {

        long found = NO_NODE;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            final long value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                found = node;
                break;
            }
        }

        return found;
    }

    final <P> long findNodeValue(BaseInnerOuterList<long[], LIST, U> list, long defaultValue, long headNode, P parameter, LongElementPredicate<P> predicate) {

        long found = defaultValue;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            final long value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                found = value;
                break;
            }
        }

        return found;
    }

    @Override
    protected final long[] toArray(LIST list, long headNode, int numElements) {

        Objects.requireNonNull(list);
        BaseList.checkIsNode(headNode);
        Checks.isNumElements(numElements);

        final long[] result = new long[numElements];

        int dstIndex = 0;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            result[dstIndex ++] = getValue(list, node);
        }

        return result;
    }
}
