package dev.jdata.db.utils.adt.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import dev.jdata.db.utils.adt.collections.BaseCollections;
import dev.jdata.db.utils.adt.collections.Coll;
import dev.jdata.db.utils.adt.elements.ByIndex;

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

    public static <T> boolean containsInstance(List<T> list, T instance) {

        return ByIndex.containsInstance(list, list.size(), instance, (b, i) -> b.get(i), IndexOutOfBoundsException::new);
    }

    public static <T> boolean containsInstance(List<T> list, int startIndex, int numElements, T instance) {

        return ByIndex.containsInstance(list, list.size(), startIndex, numElements, instance, (b, i) -> b.get(i), IndexOutOfBoundsException::new);
    }

    public static <T, U> boolean containsWithoutClosure(List<T> list, U instance, BiPredicate<U, T> predicate) {

        return containsWithoutClosure(list, instance, 0, list.size(), predicate);
    }

    public static <T, U> boolean containsWithoutClosure(List<T> list, U instance, int startIndex, int numElements, BiPredicate<U, T> predicate) {

        return ByIndex.containsWithoutClosure(list, instance, list.size(), startIndex, numElements, (byIndex, i, index) -> predicate.test(i, byIndex.get(index)),
                IndexOutOfBoundsException::new);
    }

    public static <T> int findIndex(List<T> list, Predicate<T> predicate) {

        return ByIndex.findIndex(list, list.size(), (b, i) -> predicate.test(b.get(i)), IndexOutOfBoundsException::new);
    }

    public static <T> int findIndex(List<T> list, int startIndex, int numElements, Predicate<T> predicate) {

        return ByIndex.findIndex(list, list.size(), startIndex, numElements, (b, i) -> predicate.test(b.get(i)), IndexOutOfBoundsException::new);
    }

    public static <T, U> int findIndexWithoutClosure(List<T> list, U instance, BiPredicate<U, T> predicate) {

        final int numElements = list.size();

        return ByIndex.findIndexWithoutClosure(list, instance, numElements, 0, numElements, (b, i, index) -> predicate.test(i, b.get(index)), IndexOutOfBoundsException::new);
    }

    public static <T, U> int findIndexWithoutClosure(List<T> list, U instance, int byIndexLength, int startIndex, int numElements, BiPredicate<U, T> predicate) {

        return ByIndex.findIndexWithoutClosure(list, instance, byIndexLength, startIndex, numElements, (b, i, index) -> predicate.test(i, b.get(index)),
                IndexOutOfBoundsException::new);
    }

    public static <T, U> int removeWithoutClosure(T instance, ArrayList<U> list, BiPredicate<T, U> predicate) {

        Objects.requireNonNull(instance);
        Objects.requireNonNull(list);
        Objects.requireNonNull(predicate);

        final int numElements = list.size();

        int numRemoved = 0;

        for (int i = 0; i < numElements; ++ i) {

            if (predicate.test(instance, list.get(i))) {

                list.remove(i);
            }
        }

        return numRemoved;
    }
}
