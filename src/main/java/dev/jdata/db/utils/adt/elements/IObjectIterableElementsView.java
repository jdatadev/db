package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.jdk.adt.strings.StringBuilders;

public interface IObjectIterableElementsView<T> extends IObjectElementsView<T>, IObjectIterable<T>, IObjectElementsToString<T> {

    public static <T> boolean isEmpty(IObjectIterableElementsView<T> elements) {

        Objects.requireNonNull(elements);

        return elements.forEachWithResult(Boolean.FALSE, null, null, (e, i, p) -> Boolean.FALSE);
    }

    @Override
    default <P> void toString(StringBuilder sb, P parameter, IElementsToStringAdder<T, P> consumer) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(consumer);

        sb.append(ELEMENTS_TO_STRING_PREFIX);

        forEach(sb, consumer, (e, b, c) -> {

            if (!StringBuilders.isEmpty(b)) {

                b.append(ELEMENTS_TO_STRING_SEPARATOR);
            }

            c.addString(e, b, parameter);
        });

        sb.append(ELEMENTS_TO_STRING_SUFFIX);
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
}
