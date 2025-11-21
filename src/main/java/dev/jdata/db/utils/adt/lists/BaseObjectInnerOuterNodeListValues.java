package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.IObjectContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IObjectElementPredicate;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseObjectInnerOuterNodeListValues<T> extends BaseInnerOuterNodeListValues<T[], IObjectInnerOuterNodeListInternal<T>> {

    private final IntFunction<T[]> createArray;

    private T[][] values;

    protected BaseObjectInnerOuterNodeListValues(int initialOuterCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createArray) {

        Objects.requireNonNull(createOuterArray);
        Objects.requireNonNull(createArray);

        this.createArray = createArray;

        this.values = createOuterArray.apply(initialOuterCapacity);
    }

    private T getObjectValue(int outerIndex, int innerIndex) {

        return values[outerIndex][innerIndex];
    }

    @Override
    protected void reallocateOuter(int newOuterLength) {

        Checks.isGreaterThan(newOuterLength, values.length);

        this.values = Arrays.copyOf(values, newOuterLength);
    }

    @Override
    protected void allocateInner(int outerIndex, int innerCapacity) {

        Checks.isOuterIndex(outerIndex);
        Checks.isInnerCapacity(innerCapacity);

        values[outerIndex] = createArray.apply(innerCapacity);
    }

    final T getValue(IObjectInnerOuterNodeListInternal<T> list, long node) {

        return values[list.getOuterIndex(node)][list.getInnerIndex(node)];
    }

    final void setValue(IObjectInnerOuterNodeListInternal<T> list, long node, T value) {

        values[list.getOuterIndex(node)][list.getInnerIndex(node)] = value;
    }

    final boolean containsValue(IObjectInnerOuterNodeListInternal<T> list, T value, long headNode) {

        return findValueNode(list, value, headNode, false) != NO_NODE;
    }

    final <P> boolean containsValue(IObjectInnerOuterNodeListInternal<T> list, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return findValueNode(list, headNode, false, parameter, predicate) != NO_NODE;
    }

    final boolean containsOnlyValue(IObjectInnerOuterNodeListInternal<T> list, T value, long headNode) {

        return containsOnlyValue(list, value, headNode, (i, l) -> i.equals(l));
    }

    final boolean containsOnlyValue(IObjectInnerOuterNodeListInternal<T> list, T value, long headNode, IObjectContainsOnlyPredicate<T> containsOnlyPredicate) {

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

    final long findAtMostOneValueNode(IObjectInnerOuterNodeListInternal<T> list, T value, long headNode) {

        return findValueNode(list, value, headNode, true);
    }

    private long findValueNode(IObjectInnerOuterNodeListInternal<T> list, T value, long headNode, boolean atMostOne) {

        final long noNode = NO_NODE;

        long found = noNode;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            if (Objects.equals(getValue(list, node), value)) {

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

    final <P> long findAtMostOneValueNode(IObjectInnerOuterNodeListInternal<T> list, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        return findValueNode(list, headNode, true, parameter, predicate);
    }

    private <P> long findValueNode(IObjectInnerOuterNodeListInternal<T> list, long headNode, boolean atMostOne, P parameter, IObjectElementPredicate<T, P> predicate) {

        final long noNode = NO_NODE;

        long found = noNode;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            final T value = getValue(list, node);

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

    final <P> T findAtMostOneNodeValue(IObjectInnerOuterNodeListInternal<T> list, T defaultValue, long headNode, P parameter, IObjectElementPredicate<T, P> predicate) {

        final long noNode = NO_NODE;

        long foundNode = noNode;
        T found = defaultValue;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            final T value = getValue(list, node);

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
    protected final T[] toArray(IObjectInnerOuterNodeListInternal<T> list, long headNode, int numElements) {

        Objects.requireNonNull(list);
        BaseNodeList.checkHeadNode(headNode);
        Checks.isIntNumElements(numElements);

        final T[] result = createArray.apply(numElements);

        final long noNode = NO_NODE;

        int dstIndex = 0;

        for (long node = headNode; node != noNode; node = list.getNextNode(node)) {

            result[dstIndex ++] = getValue(list, node);
        }

        return result;
    }
}
