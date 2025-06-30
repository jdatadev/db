package dev.jdata.db.utils.adt.sets;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import dev.jdata.db.utils.adt.collections.BaseCollections;
import dev.jdata.db.utils.adt.collections.Coll;

public class Sets extends BaseCollections {

    public static <T> Set<T> ofIntRange(int start, int numElements, IntFunction<T> mapper) {

        return Coll.ofIntRange(start, numElements, HashSet::new, mapper);
    }

    public static Set<Integer> of(int[] array) {

        return Coll.of(array, HashSet::new);
    }

    public static <T> Set<T> unmodifiableCopyOf(Set<T> set) {

        Objects.requireNonNull(set);

        return set.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<>(set));
    }

    public static <T, R> Set<R> map(Set<T> set, Function<T, R> mapper) {

        Objects.requireNonNull(set);
        Objects.requireNonNull(mapper);

        return map(set, mapper, HashSet::new);
    }

    public static <T, R> Set<R> filterAndMap(Set<T> set, Predicate<T> predicate, Function<T, R> mapper) {

        Objects.requireNonNull(set);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(mapper);

        return filterAndMap(set, predicate, mapper, HashSet::new);
    }

    public static <T, U> Set<U> distinct(Collection<T> collection, Function<T, U> mapper) {

        Objects.requireNonNull(collection);
        Objects.requireNonNull(mapper);

        final Set<U> result = new HashSet<>(collection.size());

        for (T element : collection) {

            result.add(mapper.apply(element));
        }

        return result;
    }
}
