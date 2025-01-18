package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.lists.IntList.ContainsOnlyPredicate;
import dev.jdata.db.utils.checks.Checks;

final class IntValues extends BaseValues<int[], BaseInnerOuterList<int[], IntValues>, IntValues> {

    private static final long NO_NODE = BaseInnerOuterList.NO_NODE;

    private int[][] values;

    IntValues(int initialOuterCapacity) {

        this.values = new int[initialOuterCapacity][];
    }

    @Override
    void reallocateOuter(int newOuterLength) {

        this.values = Arrays.copyOf(values, newOuterLength);
    }

    @Override
    void allocateInner(int outerIndex, int innerCapacity) {

        Checks.isIndex(outerIndex);
        Checks.isCapacity(innerCapacity);

        values[outerIndex] = new int[innerCapacity];
    }

    int getValue(BaseInnerOuterList<int[], IntValues> list, long node) {

        return values[list.getOuterIndex(node)][list.getInnerIndex(node)];
    }

    void setValue(BaseInnerOuterList<int[], IntValues> list, long node, int value) {

        values[list.getOuterIndex(node)][list.getInnerIndex(node)] = value;
    }

    boolean containsValue(BaseInnerOuterList<int[], IntValues> list, int value, long headNode) {

        return findValue(list, value, headNode) != NO_NODE;
    }

    boolean containsOnlyValue(BaseInnerOuterList<int[], IntValues> list, int value, long headNode) {

        return containsOnlyValue(list, value, headNode, (i, l) -> i == l);
    }

    boolean containsOnlyValue(BaseInnerOuterList<int[], IntValues> list, int value, long headNode, ContainsOnlyPredicate containsOnlyPredicate) {

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

    long findValue(BaseInnerOuterList<int[], IntValues> list, int value, long headNode) {

        long found = NO_NODE;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            if (getValue(list, node) == value) {

                found = node;
                break;
            }
        }

        return found;
    }

    @Override
    int[] toArray(BaseInnerOuterList<int[], IntValues> list, long headNode, int numElements) {

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
