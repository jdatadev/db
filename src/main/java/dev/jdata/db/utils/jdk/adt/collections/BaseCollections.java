package dev.jdata.db.utils.jdk.adt.collections;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public abstract class BaseCollections {

    protected static <T, R, C extends Collection<R>> C map(Collection<T> collection, Function<T, R> mapper, IntFunction<C> createCollection) {

        Objects.requireNonNull(collection);
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(createCollection);

        final C result = createCollection.apply(collection.size());

        for (T element : collection) {

            result.add(mapper.apply(element));
        }

        return result;
    }

    protected static <T, R, C extends Collection<R>> C filterAndMap(Collection<T> collection, Predicate<T> predicate, Function<T, R> mapper, IntFunction<C> createCollection) {

        Objects.requireNonNull(collection);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(createCollection);

        final C result = createCollection.apply(collection.size());

        for (T element : collection) {

            if (predicate.test(element)) {

                result.add(mapper.apply(element));
            }
        }

        return result;
    }
}
