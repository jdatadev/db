package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionLongConsumer;

public interface ILongSingleHeadNodeListView extends ISingleHeadNodeListView, ILongListView, IOnlyElementsView, ILongSingleHeadNodeListGetters {

    @Override
    default <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long noNode = LargeNodeLists.NO_LONG_NODE;

        for (long node = getHeadNode(); node != noNode; node = getNextNode(node)) {

            forEach.each(getValue(node), parameter);
        }
    }

    @Override
    default <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionLongConsumer<E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long noNode = LargeNodeLists.NO_LONG_NODE;

        for (long node = getHeadNode(); node != noNode; node = getNextNode(node)) {

            forEach.accept(getValue(node));
        }
    }

    @Override
    default <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long noNode = LargeNodeLists.NO_LONG_NODE;

        for (long node = getHeadNode(); node != noNode; node = getNextNode(node)) {

            final R forEachResult = forEach.each(getValue(node), parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    default <P> long findAtMostOneIndex(P parameter, ILongElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndexInRange(0L, getNumElements(), parameter, predicate);
    }

    @Deprecated
    default <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, ILongElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);
        Checks.checkFromIndexSize(startIndex, numElements, getNumElements());

        long foundIndex = -1L;

        final long endIndex = startIndex + numElements;

        final long noNode = LargeNodeLists.NO_LONG_NODE;

        long index = 0;

        for (long node = getHeadNode(); node != noNode; node = getNextNode(node)) {

            if (index >= startIndex) {

                if (index >= endIndex) {

                    break;
                }

                if (predicate.test(getValue(node), parameter)) {

                    if (foundIndex != -1L) {

                        throw ElementsExceptions.moreThanOneFoundException();
                    }

                    foundIndex = index;
                }

                ++ index;
            }
        }

        return foundIndex;
    }

    @Override
    default long getHead() {

        return getValue(getHeadNode());
    }

    @Override
    default long getTail() {

        return getValue(getTailNode());
    }

    @Override
    default long[] toArray() {

        final int numElements = IByIndexView.intIndex(getNumElements());

        final long[] result = new long[numElements];

        final long noNode = LargeNodeLists.NO_LONG_NODE;

        int dstIndex = 0;

        for (long node = getHeadNode(); node != noNode; node = getNextNode(node)) {

            result[dstIndex ++] = getValue(node);
        }

        return result;
    }
}
