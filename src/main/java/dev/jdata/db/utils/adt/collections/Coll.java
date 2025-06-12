package dev.jdata.db.utils.adt.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.checks.Checks;

public class Coll {

    public static <T, C extends Collection<T>> C ofIntRange(int start, int numElements, IntFunction<C> createCollection, IntFunction<T> mapper) {

        Checks.isAboveZero(numElements);
        Objects.requireNonNull(createCollection);
        Objects.requireNonNull(mapper);

        final C result = createCollection.apply(numElements);

        for (int i = 0; i < numElements; ++ i) {

            result.add(mapper.apply(start + i));
        }

        return result;
    }

    public static <T> int maxInt(Iterable<T> iterable, int defaultValue, ToIntFunction<T> mapper) {

        Objects.requireNonNull(iterable);
        Objects.requireNonNull(mapper);

        int max = Integer.MIN_VALUE;
        boolean found = false;

        for (T element : iterable) {

            final int value = mapper.applyAsInt(element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    public static <T> long maxLong(Iterable<T> iterable, long defaultValue, ToLongFunction<T> mapper) {

        Objects.requireNonNull(iterable);
        Objects.requireNonNull(mapper);

        long max = Long.MIN_VALUE;
        boolean found = false;

        for (T element : iterable) {

            final long value = mapper.applyAsLong(element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    public static <T, P> long count(Iterable<T> iterable, P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(iterable);
        Objects.requireNonNull(predicate);

        long count = 0;

        for (T element : iterable) {

            if (predicate.test(element, parameter)) {

                ++ count;
            }
        }

        return count;
    }

    public static <T> long closureOrConstantCount(Iterable<T> iterable, Predicate<T> predicate) {

        Objects.requireNonNull(iterable);
        Objects.requireNonNull(predicate);

        return count(iterable, predicate, (e, p) -> p.test(e));
    }

    public static <T> List<T> sorted(Collection<T> collection, Comparator<T> comparator) {

        Objects.requireNonNull(collection);
        Objects.requireNonNull(comparator);

        final List<T> result = new ArrayList<>(collection);

        result.sort(comparator);

        return result;
    }

    public static <T> List<T> unmodifiableSorted(Collection<T> collection, Comparator<T> comparator) {

        return Collections.unmodifiableList(sorted(collection, comparator));
    }

    public static <T> T[] toArray(Collection<T> collection, IntFunction<T[]> createArray) {

        Objects.requireNonNull(collection);
        Objects.requireNonNull(createArray);

        final T[] result = createArray.apply(collection.size());

        int dstIndex = 0;

        for (T element : collection) {

            result[dstIndex ++] = element;
        }

        return result;
    }

    @FunctionalInterface
    public interface CollectionElementGetter<T, C> {

        T getAt(C collection, int index);
    }

    public static <T, C> T[] toArray(C collection, int numElements, IntFunction<T[]> createArray, CollectionElementGetter<T, C> elementGetter) {

        Objects.requireNonNull(collection);
        Checks.isNumElements(numElements);
        Objects.requireNonNull(elementGetter);

        final T[] result = createArray.apply(numElements);

        for (int i = 0; i < numElements; ++ i) {

            result[i] = elementGetter.getAt(collection, i);
        }

        return result;
    }
}
