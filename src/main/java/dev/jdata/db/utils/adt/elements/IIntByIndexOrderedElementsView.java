package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.IIntByIndexView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionIntConsumer;

public interface IIntByIndexOrderedElementsView extends IIntOrderedElementsView, IIntByIndexView, IByIndexOrderedElementsView, IIntByIndexOrderedElementsGetters {

    @Override
    default void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    default <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            forEach.each(get(i), parameter);
        }
    }

    @Override
    default <E extends Exception> void closureOrConstantForEach(CheckedExceptionIntConsumer<E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            forEach.accept(get(i));
        }
    }

    @Override
    default <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            final R forEachResult = forEach.each(get(i), parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    default <P> long findAtMostOneIndex(P parameter, IIntElementPredicate<P> predicate) {

        return findAtMostOneIndexInRange(0L, getIndexLimit(), parameter, predicate);
    }

    @Override
    default <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, IIntElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);
        Checks.checkFromIndexSize(startIndex, numElements, getIndexLimit());

        long foundIndex = -1L;

        final long endIndex = startIndex + numElements;

        for (long i = startIndex; i < endIndex; ++ i) {

            if (predicate.test(get(i), parameter)) {

                if (foundIndex != -1L) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                foundIndex = i;
            }
        }

        return foundIndex;
    }

    @Override
    default int[] toArray() {

        return IntElementsHelper.toArrayIndexView(this, IByIndexOrderedElementsView.intIndexLimit(this));
    }
}
