package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

public interface IObjectOrderedOnlyElementsView<T> extends IObjectOrderedElementsView<T>, IObjectIterableOnlyElementsView<T> {

    default <B extends IObjectOrderedOnlyElementsBuilder<R, V, ?>, V extends IObjectOrderedOnlyElementsView<R>, R> V mapOrEmpty(LongFunction<B> createBuilder,
            Function<T, R> mapper, Supplier<V> emptySupplier) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(emptySupplier);

        final V mapped = mapOrNull(createBuilder, mapper);

        return mapped != null ? mapped : emptySupplier.get();
    }

    default <B extends IObjectOrderedOnlyElementsBuilder<R, V, ?>, V extends IObjectOrderedOnlyElementsView<R>, R> V mapOrNull(LongFunction<B> createBuilder,
            Function<T, R> mapper) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);

        final long numElements = getNumElements();

        final B builder = createBuilder.apply(numElements);

        map(builder, mapper);

        return builder.buildOrNull();
    }
}
