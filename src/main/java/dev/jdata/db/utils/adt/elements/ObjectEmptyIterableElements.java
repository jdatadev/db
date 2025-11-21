package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.function.CheckedExceptionConsumer;

abstract class ObjectEmptyIterableElements<T> extends ObjectEmptyElements<T> implements IObjectIterableElements<T> {

    @Override
    public final <P> void toString(StringBuilder sb, P parameter, IElementsToStringAdder<T, P> consumer) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(consumer);

        sb.append(ELEMENTS_TO_STRING_PREFIX);
        sb.append(ELEMENTS_TO_STRING_SUFFIX);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IObjectForEach<T, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);
    }

    @Override
    public final <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionConsumer<T, E> forEach) throws E {

        Objects.requireNonNull(forEach);
    }

    @Override
    public final <P1, P2, E extends Exception> void forEach(P1 parameter1, P2 parameter2, IObjectForEach2<T, P1, P2, E> forEach) throws E {

        Objects.requireNonNull(forEach);
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IObjectForEachWithResult<T, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        return null;
    }

    @Override
    public final <P> long count(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return 0L;
    }

    @Override
    public final long closureOrConstantCount(Predicate<T> predicate) {

        Objects.requireNonNull(predicate);

        return 0L;
    }

    @Override
    public final int maxInt(int defaultValue, ToIntFunction<? super T> mapper) {

        Objects.requireNonNull(mapper);

        return defaultValue;
    }

    @Override
    public final long maxLong(long defaultValue, ToLongFunction<? super T> mapper) {

        Objects.requireNonNull(mapper);

        return defaultValue;
    }

    @Override
    public final T[] toArray(IntFunction<T[]> createArray) {

        Objects.requireNonNull(createArray);

        return createArray.apply(0);
    }

    @Override
    public final <P> T findAtMostOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return null;
    }

    @Override
    public final <P> T findExactlyOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return null;
    }
}
