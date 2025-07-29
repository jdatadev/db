package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectElements.IContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IObjectElements.IObjectElementPredicate;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseObjectValues<T, LIST extends BaseLargeList<T[], LIST, VALUES>, VALUES extends BaseObjectValues<T, LIST, VALUES>>

        extends BaseValues<T[], LIST, VALUES> {

    private final IntFunction<T[]> createArray;

    private T[][] values;

    protected BaseObjectValues(int initialOuterCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {

        Objects.requireNonNull(createOuterArray);
        Objects.requireNonNull(createArray);

        this.createArray = createArray;

        this.values = createOuterArray.apply(initialOuterCapacity);
    }

    public final T getObjectValue(int outerIndex, int innerIndex) {

        return values[outerIndex][innerIndex];
    }

    @Override
    protected final void reallocateOuter(int newOuterLength) {

        Checks.isGreaterThan(newOuterLength, values.length);

        this.values = Arrays.copyOf(values, newOuterLength);
    }

    @Override
    protected final void allocateInner(int outerIndex, int innerCapacity) {

        Checks.isIndex(outerIndex);
        Checks.isCapacity(innerCapacity);

        values[outerIndex] = createArray.apply(innerCapacity);
    }

    protected final T getValue(BaseInnerOuterList<T[], LIST, VALUES> list, long node) {

        return values[list.getOuterIndex(node)][list.getInnerIndex(node)];
    }

    protected final void setValue(BaseInnerOuterList<T[], LIST, VALUES> list, long node, T value) {

        values[list.getOuterIndex(node)][list.getInnerIndex(node)] = value;
    }

    final boolean containsValue(BaseInnerOuterList<T[], LIST, VALUES> list, T value, long headNode) {

        return findValueNode(list, value, headNode) != NO_NODE;
    }

    final <P> boolean containsValue(BaseInnerOuterList<T[], LIST, VALUES> list, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return findValueNode(list, headNode, parameter, predicate) != NO_NODE;
    }

    final boolean containsOnlyValue(BaseInnerOuterList<T[], LIST, VALUES> list, T value, long headNode) {

        return containsOnlyValue(list, value, headNode, (i, l) -> i == l);
    }

    final boolean containsOnlyValue(BaseInnerOuterList<T[], LIST, VALUES> list, T value, long headNode, IContainsOnlyPredicate<T> containsOnlyPredicate) {

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

    final long findValueNode(BaseInnerOuterList<T[], LIST, VALUES> list, T value, long headNode) {

        long found = NO_NODE;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            if (Objects.equals(getValue(list, node), value)) {

                found = node;
                break;
            }
        }

        return found;
    }

    final <P> long findValueNode(BaseInnerOuterList<T[], LIST, VALUES> list, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        long found = NO_NODE;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            final T value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                found = node;
                break;
            }
        }

        return found;
    }

    final <P> T findNodeValue(BaseInnerOuterList<T[], LIST, VALUES> list, T defaultValue, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        T found = defaultValue;

        for (long node = headNode; node != NO_NODE; node = list.getNextNode(node)) {

            final T value = getValue(list, node);

            if (predicate.test(value, parameter)) {

                found = value;
                break;
            }
        }

        return found;
    }

    @Override
    protected final T[] toArray(LIST list, long headNode, int numElements) {

        Objects.requireNonNull(list);
        BaseList.checkIsNode(headNode);
        Checks.isNumElements(numElements);

        final T[] result = createArray.apply(numElements);

        final long noNode = NO_NODE;

        int dstIndex = 0;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            result[dstIndex ++] = getValue(list, node);
        }

        return result;
    }
}
