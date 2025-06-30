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
import dev.jdata.db.utils.adt.elements.ByIndex.IByIndexElementEqualityTester;

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

    public static List<Integer> of(int[] array) {

        return Coll.of(array, ArrayList::new);
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

            result = ByIndex.containsInstance(list, list.size(), instance, (l, i) -> l.get((int)i), IndexOutOfBoundsException::new);
        }
        else {
            Objects.requireNonNull(instance);

            result = false;
        }

        return result;
    }

    public static <T> boolean containsInstance(List<T> list, T instance, int startIndex, int numElements) {

        return ByIndex.containsInstance(list, list.size(), instance, startIndex, numElements, (l, i) -> l.get((int)i), IndexOutOfBoundsException::new);
    }

    public static <T, P> boolean contains(List<T> list, P parameter, BiPredicate<T, P> predicate) {

        return contains(list, 0, list.size(), parameter, predicate);
    }

    public static <T, P> boolean contains(List<T> list, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.contains(list, list.size(), startIndex, numElements, parameter, (l, i) -> l.get((int)i), predicate, IndexOutOfBoundsException::new);
    }

    public static <T, P> int findAtMostOneIndex(List<T> list, P parameter, BiPredicate<T, P> predicate) {

        return (int)ByIndex.findAtMostOneIndex(list, list.size(), parameter, (l, i) -> l.get((int)i), predicate, IndexOutOfBoundsException::new);
    }

    public static <T> int closureOrConstantFindAtMostOneIndex(List<T> list, Predicate<T> predicate) {

        return (int)ByIndex.findAtMostOneIndex(list, list.size(), predicate, (l, i) -> l.get((int)i), (e, p) -> p.test(e), IndexOutOfBoundsException::new);
    }

    public static <T, P> int findAtMostOneIndex(List<T> list, int byIndexLength, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return (int)ByIndex.findAtMostOneIndex(list, byIndexLength, startIndex, numElements, parameter, (l, i) -> l.get((int)i), predicate, IndexOutOfBoundsException::new);
    }

    public static <T> int closureOrConstantFindAtMostOneIndex(List<T> list, int startIndex, int numElements, Predicate<T> predicate) {

        return (int)ByIndex.findAtMostOneIndex(list, list.size(), startIndex, numElements, predicate, (l, i) -> l.get((int)i), predicate != null ? (e, p) -> p.test(e) : null,
                IndexOutOfBoundsException::new);
    }

    public static <T, P> int remove(ArrayList<T> list, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(list);
        Objects.requireNonNull(predicate);

        final int numElements = list.size();

        int numRemoved = 0;

        for (int i = numElements - 1; i >= 0; -- i) {

            if (predicate.test(list.get(i), parameter)) {

                list.remove(i);

                ++ numRemoved;
            }
        }

        return numRemoved;
    }

    @FunctionalInterface
    public interface IListEqualityTester<T, P1, P2> extends IByIndexElementEqualityTester<T, P1, P2> {

    }

    public static <T, P1, P2> boolean equals(List<T> list1, P1 parameter1, List<T> list2, P2 parameter2, IListEqualityTester<T, P1, P2> equalityTester) {

        final boolean result;

        final int array1Length = list1.size();

        if (array1Length == list2.size()) {

            result = equals(list1, 0, parameter1, list2, 0, parameter2, array1Length, equalityTester);
        }
        else {
            result = false;
        }

        return result;
    }

    public static <T, P1, P2> boolean equals(List<T> list1, int startIndex1, P1 parameter1, List<T> list2, int startIndex2, P2 parameter2, int numElements,
            IListEqualityTester<T, P1, P2> equalityTester) {

        return ByIndex.equals(list1, startIndex1, parameter1, list2, startIndex2, parameter2, numElements, equalityTester,
                (l1, i1, p1, l2, i2, p2, predicate) -> predicate.equals(l1.get((int)i1), p1, l2.get((int)i2), p2));
    }
}
