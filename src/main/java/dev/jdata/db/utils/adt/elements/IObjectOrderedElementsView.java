package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import dev.jdata.db.utils.adt.elements.builders.IObjectOrderedElementsBuilder;

public interface IObjectOrderedElementsView<T> extends IObjectIterableElementsView<T>, IObjectOrderedElementsGetters<T> {

    @Override
    default <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper) {

        Objects.requireNonNull(addable);
        Objects.requireNonNull(mapper);

        forEach(addable, mapper, (e, a, m) -> m.apply(e));
    }

    default <B extends IObjectOrderedElementsBuilder<R, V>, V extends IObjectOrderedElementsView<T>, R> V mapOrEmpty(LongFunction<B> createBuilder, Function<T, R> mapper,
            Supplier<V> emptySupplier) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(emptySupplier);

        final V mapped = mapOrNull(createBuilder, mapper);

        return mapped != null ? mapped : emptySupplier.get();
    }

    default <B extends IObjectOrderedElementsBuilder<R, V>, V extends IObjectOrderedElementsView<T>, R> V mapOrNull(LongFunction<B> createBuilder, Function<T, R> mapper) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);

        final long numElements = getNumElements();

        final B builder = createBuilder.apply(numElements);

        map(builder, mapper);

        return builder.buildOrNull();
    }
}
