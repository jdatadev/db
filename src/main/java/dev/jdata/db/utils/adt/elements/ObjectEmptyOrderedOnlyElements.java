package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Supplier;

public abstract class ObjectEmptyOrderedOnlyElements<T> extends ObjectEmptyOrderedElements<T> implements IObjectOrderedOnlyElements<T> {

    @Override
    public final <B extends IObjectOrderedOnlyElementsBuilder<R, V, ?>, V extends IObjectOrderedOnlyElementsView<R>, R> V mapOrEmpty(LongFunction<B> createBuilder,
            Function<T, R> mapper, Supplier<V> emptySupplier) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(emptySupplier);

        return emptySupplier.get();
    }

    @Override
    public final <B extends IObjectOrderedOnlyElementsBuilder<R, V, ?>, V extends IObjectOrderedOnlyElementsView<R>, R> V mapOrNull(LongFunction<B> createBuilder,
            Function<T, R> mapper) {

        Objects.requireNonNull(createBuilder);
        Objects.requireNonNull(mapper);

        return null;
    }
}
