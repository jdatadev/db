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

        final boolean result;

        final int listSize = list.size();

        if (listSize != 0) {

            result = ByIndex.containsInstance(list, list.size(), instance, List::get, IndexOutOfBoundsException::new);
        }
        else {
            Objects.requireNonNull(instance);

            result = false;
        }

        return result;
    }

    public static <T> boolean containsInstance(List<T> list, int startIndex, int numElements, T instance) {

        return ByIndex.containsInstance(list, list.size(), startIndex, numElements, instance, List::get, IndexOutOfBoundsException::new);
    }

    public static <T, P> boolean contains(List<T> list, P parameter, BiPredicate<T, P> predicate) {

        return contains(list, 0, list.size(), parameter, predicate);
    }

    public static <T, P> boolean contains(List<T> list, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.contains(list, list.size(), startIndex, numElements, parameter, List::get, predicate, IndexOutOfBoundsException::new);
    }

    public static <T, P> int findIndex(List<T> list, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.findIndex(list, list.size(), parameter, List::get, predicate, IndexOutOfBoundsException::new);
    }

    public static <T> int closureOrConstantFindIndex(List<T> list, Predicate<T> predicate) {

        return ByIndex.findIndex(list, list.size(), predicate, List::get, (e, p) -> p.test(e), IndexOutOfBoundsException::new);
    }

    public static <T, P> int findIndex(List<T> list, int byIndexLength, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.findIndex(list, byIndexLength, startIndex, numElements, parameter, List::get, predicate, IndexOutOfBoundsException::new);
    }

    public static <T> int closureOrConstantFindIndex(List<T> list, int startIndex, int numElements, Predicate<T> predicate) {

        return ByIndex.findIndex(list, list.size(), startIndex, numElements, predicate, List::get, predicate != null ? (e, p) -> p.test(e) : null,
                IndexOutOfBoundsException::new);
    }

    public static <T, P> int remove(ArrayList<T> list, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(list);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(parameter);

        final int numElements = list.size();

        int numRemoved = 0;

        for (int i = 0; i < numElements; ++ i) {

            if (predicate.test(list.get(i), parameter)) {

                list.remove(i);
            }
        }

        return numRemoved;
    }
}
