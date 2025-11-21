package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Function;

public interface IObjectOrderedElementsView<T> extends IObjectIterableElementsView<T>, IObjectOrderedElementsGetters<T> {

    @Override
    default <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper) {

        Objects.requireNonNull(addable);
        Objects.requireNonNull(mapper);

        forEach(addable, mapper, (e, a, m) -> m.apply(e));
    }
}
