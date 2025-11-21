package dev.jdata.db.utils.adt.elements;

import java.util.Comparator;
import java.util.Objects;

import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexListAllocator;

public interface IObjectIterableElementsView<T> extends IObjectElementsView<T>, IObjectIterable<T> {

    public static <T> boolean isEmpty(IObjectIterableElementsView<T> elements) {

        Objects.requireNonNull(elements);

        return elements.forEachWithResult(Boolean.FALSE, null, null, (e, i, p) -> Boolean.FALSE);
    }

    @Override
    default boolean contains(T value) {

        Objects.requireNonNull(value);

        return contains(value, (e, v) -> e.equals(v));
    }

    @Override
    default <P> boolean contains(P parameter, IObjectElementPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return forEachWithResult(Boolean.FALSE, parameter, predicate, (element, passedParameer, passedPredicate) -> passedPredicate.test(element, passedParameer));
    }

    @Override
    default boolean containsOnly(T value) {

        Objects.requireNonNull(value);

        return forEachWithResult(Boolean.TRUE, value, null, (e, v, p2) -> !e.equals(v) ? Boolean.FALSE : null);
    }

    @Override
    default boolean containsOnly(T value, IObjectContainsOnlyPredicate<T> predicate) {

        Objects.requireNonNull(value);
        Objects.requireNonNull(predicate);

        return forEachWithResult(Boolean.TRUE, value, predicate, (e, v, p) -> !p.test(v, e) ? Boolean.FALSE : null);
    }

    @Override
    default boolean containsInstance(T instance) {

        Objects.requireNonNull(instance);

        return forEachWithResult(Boolean.FALSE, instance, null, (e, i, p) -> e == i ? Boolean.TRUE : null);
    }

    @Deprecated // dst?
    default IndexList<T> sorted(Comparator<? super T> comparator, IndexListAllocator<T, ? extends IndexList<T>, ?, ?, ?, ?> indexListAllocator) {

        return IndexList.sortedOf(this, comparator, indexListAllocator);
    }
}
