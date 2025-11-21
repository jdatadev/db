package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import dev.jdata.db.utils.adt.elements.builders.IObjectOrderedElementsBuilder;
import dev.jdata.db.utils.checks.Checks;

public abstract class ObjectEmptyOrderedElements<T> extends ObjectEmptyIterableElements<T> implements IObjectOrderedElementsView<T> {

    @Override
    public final <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper) {

        Objects.requireNonNull(addable);
        Objects.requireNonNull(mapper);
    }

    @Override
    public final <B extends IObjectOrderedElementsBuilder<R, V>, V extends IObjectOrderedElementsView<T>, R> V mapOrEmpty(LongFunction<B> createBuilder, Function<T, R> mapper,
            Supplier<V> emptySupplier) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(emptySupplier);

        return emptySupplier.get();
    }

    @Override
    public final <B extends IObjectOrderedElementsBuilder<R, V>, V extends IObjectOrderedElementsView<T>, R> V mapOrNull(LongFunction<B> createBuilder, Function<T, R> mapper) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);

        return null;
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndex(Predicate<T> predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, Predicate<T> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }
}
