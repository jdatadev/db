package dev.jdata.db.utils.adt.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import dev.jdata.db.utils.adt.collections.BaseCollections;
import dev.jdata.db.utils.adt.collections.Coll;

public class Lists extends BaseCollections {

    public static <T> List<T> empty() {

        return Collections.emptyList();
    }

    @SafeVarargs
    public static <T> List<T> unmodifiableOf(T ... elements) {

        return unmodifiableOf(Arrays.asList(elements));
    }

    public static <T> List<T> unmodifiableOf(List<T> list) {

        return Collections.unmodifiableList(list);
    }

    public static <T> List<T> ofIntRange(int start, int numElements, IntFunction<T> mapper) {

        return Coll.ofIntRange(start, numElements, ArrayList::new, mapper);
    }

    public static <T> List<T> unmodifiableCopyOf(List<T> list) {

        Objects.requireNonNull(list);

        return list.isEmpty() ? empty() : unmodifiableOf(new ArrayList<T>(list));
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {

        Objects.requireNonNull(list);
        Objects.requireNonNull(mapper);

        return map(list, mapper, ArrayList::new);
    }

    public static <T, R> List<R> filterAndMap(List<T> list, Predicate<T> predicate, Function<T, R> mapper) {

        Objects.requireNonNull(list);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(mapper);

        return filterAndMap(list, predicate, mapper, ArrayList::new);
    }
}
