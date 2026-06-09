package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.IElementEqualityTester;
import dev.jdata.db.utils.adt.elements.IObjectForEachWithResult;
import dev.jdata.db.utils.adt.elements.IObjectOrderedElementsView;

abstract class BaseObjectLinkedList<T, N extends ObjectSinglyLinkedNode<T, N, L>, L extends BaseObjectLinkedList<T, N, L>>

        extends BaseLinkedList<N, L>
        implements IObjectLinkedListCommon<T> {

    BaseObjectLinkedList(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final <P> long count(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        long count = 0L;

        for (N node = getListHeadNode(); node != null; node = node.next) {

            if (predicate.test(node.element, parameter)) {

                ++ count;
            }
        }

        return count;
    }

    @Override
    public final int maxInt(int defaultValue, ToIntFunction<? super T> mapper) {

        Objects.requireNonNull(mapper);

        int max = Integer.MIN_VALUE;
        boolean found = false;

        for (N node = getListHeadNode(); node != null; node = node.next) {

            final int value = mapper.applyAsInt(node.element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    @Override
    public final long maxLong(long defaultValue, ToLongFunction<? super T> mapper) {

        Objects.requireNonNull(mapper);

        long max = Long.MIN_VALUE;
        boolean found = false;

        for (N node = getListHeadNode(); node != null; node = node.next) {

            final long value = mapper.applyAsLong(node.element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    @Override
    public final <P> T findAtMostOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        T found = null;

        for (N node = getListHeadNode(); node != null; node = node.next) {

            final T element = node.element;

            if (predicate.test(element, parameter)) {

                if (found != null) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                found = element;
            }
        }

        return found;
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IObjectForEachWithResult<T, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        for (N node = getListHeadNode(); node != null; node = node.next) {

            final R forEachResult = forEach.each(node.element, parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    public final <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        long foundIndex = -1L;

        long index = 0L;

        for (N node = getListHeadNode(); node != null; node = node.next) {

            if (predicate.test(node.element, parameter)) {

                if (foundIndex != -1L) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                foundIndex = index;
            }

            ++ index;
        }

        return foundIndex;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IObjectOrderedElementsView<T> other, P2 otherParameter,
            IElementEqualityTester<T, P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean equals;

        final long thisNumElements = getNumElements();

        if (other instanceof BaseObjectLinkedList<?, ?, ?>) {

            final BaseObjectLinkedList<T, ?, ?> otherLinkedList = (BaseObjectLinkedList<T, ?, ?>)other;

            if (thisNumElements != otherLinkedList.getNumElements()) {

                equals = false;
            }
            else {
                boolean nodesEqual = true;

                N thisNode = getListHeadNode();
                ObjectSinglyLinkedNode<T, ?, ?> otherNode = otherLinkedList.getListHeadNode();

                for (; thisNode != null; thisNode = thisNode.next, otherNode = otherNode.next) {

                    if (!thisNode.element.equals(otherNode.element)) {

                        nodesEqual = false;
                        break;
                    }
                }

                equals = nodesEqual;
            }
        }
        else {
            throw new UnsupportedOperationException();
        }

        return equals;
    }

    @Override
    public final T getHead() {

        final N headNode = getListHeadNode();

        if (headNode == null) {

            throw ElementsExceptions.emptyException();
        }

        return headNode.element;
    }

    @Override
    public final T getTail() {

        final N tailNode = getListTailNode();

        if (tailNode == null) {

            throw ElementsExceptions.emptyException();
        }

        return tailNode.element;
    }

    final void checkNode(N node) {

        if (node.list != this) {

            throw new IllegalArgumentException();
        }
    }
}
