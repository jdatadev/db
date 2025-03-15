package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public class ByIndex {

    @FunctionalInterface
    public interface ByIndexGetter<T, U> {

        T get(U byIndex, int index);
    }

    @FunctionalInterface
    public interface ByIndexSetter<T, U> {

       void set(U byIndex, int index, T element);
    }

    public static <T, U> boolean containsInstance(U byIndex, int byIndexLength, T instance, ByIndexGetter<T, U> getter,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return findIndex(byIndex, byIndexLength, 0, byIndexLength, instance, getter, (e, p) -> e == p, exceptionSupplier) != -1;
    }

    public static <T, U> boolean containsInstance(U byIndex, int byIndexLength, int startIndex, int numElements, T instance, ByIndexGetter<T, U> getter,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(instance);

        return findIndex(byIndex, byIndexLength, startIndex, numElements, instance, (b, i) -> getter.get(b, i), (e, p) -> e == p, exceptionSupplier) != -1;
    }

    public static <T, U, P> boolean contains(U byIndex, int byIndexLength, P parameter, ByIndexGetter<T, U> getter, BiPredicate<T, P> predicate,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return findIndex(byIndex, byIndexLength, 0, byIndexLength, parameter, getter, predicate, exceptionSupplier) != -1;
    }

    public static <T, U, P> boolean contains(U byIndex, int byIndexLength, int startIndex, int numElements, P parameter, ByIndexGetter<T, U> getter, BiPredicate<T, P> predicate,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return findIndex(byIndex, byIndexLength, startIndex, numElements, parameter, getter, predicate, exceptionSupplier) != -1;
    }

    public static <T, U, P> int findIndex(U byIndex, int byIndexLength, P parameter, ByIndexGetter<T, U> getter, BiPredicate<T, P> predicate,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return findIndex(byIndex, byIndexLength, 0, byIndexLength, parameter, getter, predicate, exceptionSupplier);
    }

    public static <T, U, P> int findIndex(U byIndex, int byIndexLength, int startIndex, int numElements, P parameter, ByIndexGetter<T, U> getter, BiPredicate<T, P> predicate,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(byIndex);
        Checks.isLengthAboveOrAtZero(byIndexLength);
        Objects.requireNonNull(predicate);

        if (startIndex < 0) {

            throw exceptionSupplier.get();
        }

        Checks.isNumElements(numElements);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(exceptionSupplier);

        final int endIndex = startIndex + numElements;

        if (byIndexLength == 0) {

            throw exceptionSupplier.get();
        }
        else if (startIndex >= byIndexLength) {

            throw exceptionSupplier.get();
        }
        else if (endIndex > byIndexLength) {

            throw exceptionSupplier.get();
        }

        int foundIndex = -1;

        for (int i = startIndex; i < endIndex; ++ i) {

            final T element = getter.get(byIndex, i);

            if (predicate.test(element, parameter)) {

                foundIndex = i;
                break;
            }
        }

        return foundIndex;
    }

    @FunctionalInterface
    public interface ByIndexStringAdderPredicate<T> {

        boolean test(T instance, int index);
    }

    public static <T, U, V, R> void map(U byIndex, int numElements, ByIndexGetter<T, U> getter, Function<T, R> mapper, ByIndexSetter<R, V> setter, V dst) {

        for (int i = 0; i < numElements; ++ i) {

            final T value = getter.get(byIndex, i);

            final R mapped = mapper.apply(value);

            setter.set(dst, i, mapped);
        }
    }

    @FunctionalInterface
    public interface ByIndexStringAdder<T> {

        void addString(T instance, int index, StringBuilder sb);
    }

    public static <T> String toString(T byIndex, int startIndex, int numElements, BiConsumer<T, StringBuilder> prefixAdder, ByIndexStringAdder<T> byIndexStringAdder) {

        return largeToString(byIndex, (long)startIndex, (long)numElements, prefixAdder, null, (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    public static <T> String toString(T byIndex, int startIndex, int numElements, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, ByIndexStringAdder<T> byIndexStringAdder) {

        return largeToString(byIndex, (long)startIndex, (long)numElements, prefixAdder, (a, i) -> byIndexStringAdderPredicate.test(a, (int)i),
                (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    public static <T> void toString(T byIndex, int startIndex, int numElements, StringBuilder sb, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, ByIndexStringAdder<T> byIndexStringAdder) {

        largeToString(byIndex, (long)startIndex, (long)numElements, sb, prefixAdder, (a, i) -> byIndexStringAdderPredicate.test(a, (int)i),
                (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    @FunctionalInterface
    public interface LargeByIndexStringAdderPredicate<T> {

        boolean test(T instance, long index);
    }

    @FunctionalInterface
    public interface LargeByIndexStringAdder<T> {

        void addString(T instance, long index, StringBuilder sb);
    }

    public static <T> String largeToString(T byIndex, long startIndex, long numElements, BiConsumer<T, StringBuilder> prefixAdder,
            LargeByIndexStringAdder<T> byIndexStringAdder) {

        return largeToString(byIndex, startIndex, numElements, prefixAdder, null, byIndexStringAdder);
    }

    public static <T> String largeToString(T byIndex, long startIndex, long numElements, BiConsumer<T, StringBuilder> prefixAdder,
            LargeByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, LargeByIndexStringAdder<T> byIndexStringAdder) {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(numElements * 10));

        largeToString(byIndex, startIndex, numElements, sb, prefixAdder, byIndexStringAdderPredicate, byIndexStringAdder);

        return sb.toString();
    }

    public static <T> void largeToString(T byIndex, long startIndex, long numElements, StringBuilder sb, BiConsumer<T, StringBuilder> prefixAdder,
            LargeByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, LargeByIndexStringAdder<T> byIndexStringAdder) {

        Objects.requireNonNull(byIndex);
        Checks.isIndex(startIndex);
        Checks.isNumElements(numElements);
        Objects.requireNonNull(byIndexStringAdder);

        if (prefixAdder != null) {

            prefixAdder.accept(byIndex, sb);

            if (!sb.isEmpty()) {

                sb.append(' ');
            }
        }

        sb.append('[');

        final int prefixLength = sb.length();

        for (long i = 0; i < numElements; ++ i) {

            final long index = startIndex + i;

            if (byIndexStringAdderPredicate == null || byIndexStringAdderPredicate.test(byIndex, index)) {

                if (sb.length() > prefixLength) {

                    sb.append(',');
                }

                byIndexStringAdder.addString(byIndex, index, sb);
            }
        }

        sb.append(']');
    }
}
