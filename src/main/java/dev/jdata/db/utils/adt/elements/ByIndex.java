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

       void set(U byIndex, int index, T value);
    }

    public static <T, U> boolean containsInstance(U byIndex, int byIndexLength, T instance, ByIndexGetter<T, U> getter,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(instance);

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

        Objects.requireNonNull(byIndex);
        Checks.isLengthAboveOrAtZero(byIndexLength);
        Objects.requireNonNull(getter);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(exceptionSupplier);

        return findIndex(byIndex, byIndexLength, 0, byIndexLength, parameter, getter, predicate, exceptionSupplier);
    }

    public static <T, U, P> int findIndex(U byIndex, int byIndexLength, int startIndex, int numElements, P parameter, ByIndexGetter<T, U> getter, BiPredicate<T, P> predicate,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(byIndex);
        Checks.isLengthAboveOrAtZero(byIndexLength);

        final int endIndex;

        if (startIndex < 0) {

            throw exceptionSupplier.get();
        }
        else if (startIndex >= byIndexLength && byIndexLength != 0) {

            throw exceptionSupplier.get();
        }
        else if ((endIndex = startIndex + numElements) > byIndexLength) {

            throw exceptionSupplier.get();
        }

        Checks.isNumElements(numElements);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(exceptionSupplier);

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

    public static <T> String closureOrConstantToString(T byIndex, int startIndex, int numElements, BiConsumer<T, StringBuilder> prefixAdder, ByIndexStringAdder<T> byIndexStringAdder) {

        return closureOrConstantLargeToString(byIndex, (long)startIndex, (long)numElements, prefixAdder, null, (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    public static <T> String closureOrConstantToString(T byIndex, int startIndex, int numElements, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, ByIndexStringAdder<T> byIndexStringAdder) {

        return closureOrConstantLargeToString(byIndex, (long)startIndex, (long)numElements, prefixAdder, (a, i) -> byIndexStringAdderPredicate.test(a, (int)i),
                (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    public static <T> void closureOrConstantToString(T byIndex, int startIndex, int numElements, StringBuilder sb, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, ByIndexStringAdder<T> byIndexStringAdder) {

        closureOrConstantLargeToString(byIndex, (long)startIndex, (long)numElements, sb, prefixAdder, (a, i) -> byIndexStringAdderPredicate.test(a, (int)i),
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

    public static <T> String closureOrConstantLargeToString(T byIndex, long startIndex, long numElements, BiConsumer<T, StringBuilder> prefixAdder,
            LargeByIndexStringAdder<T> byIndexStringAdder) {

        return closureOrConstantLargeToString(byIndex, startIndex, numElements, prefixAdder, null, byIndexStringAdder);
    }

    public static <T> String closureOrConstantLargeToString(T byIndex, long startIndex, long numElements, BiConsumer<T, StringBuilder> prefixAdder,
            LargeByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, LargeByIndexStringAdder<T> byIndexStringAdder) {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(numElements * 10));

        closureOrConstantLargeToString(byIndex, startIndex, numElements, sb, prefixAdder, byIndexStringAdderPredicate, byIndexStringAdder);

        return sb.toString();
    }

    public static <T> void closureOrConstantLargeToString(T byIndex, long startIndex, long numElements, StringBuilder sb, BiConsumer<T, StringBuilder> prefixAdder,
            LargeByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, LargeByIndexStringAdder<T> byIndexStringAdder) {

        largeToString(byIndex, startIndex, numElements, sb, byIndexStringAdderPredicate, byIndexStringAdder, prefixAdder,
                byIndexStringAdderPredicate != null ? (instance, index, parameter) -> parameter.test(instance, index) : null,
                (instance, index, b, parameter) -> parameter.addString(instance, index, b));
    }

    @FunctionalInterface
    public interface LargeByIndexStringAdderParameterPredicate<T, P> {

        boolean test(T instance, long index, P parameter);
    }

    @FunctionalInterface
    public interface LargeByIndexStringParameterAdder<T, P> {

        void addString(T instance, long index, StringBuilder sb, P parameter);
    }

    public static <T, U, V> void largeToString(T byIndex, long startIndex, long numElements, StringBuilder sb, U predicateParameter, V adderParameter,
            BiConsumer<T, StringBuilder> prefixAdder, LargeByIndexStringAdderParameterPredicate<T, U> byIndexStringAdderParameterPredicate,
            LargeByIndexStringParameterAdder<T, V> byIndexStringParameterAdder) {

        Objects.requireNonNull(byIndex);
        Checks.isIndex(startIndex);
        Checks.isNumElements(numElements);
        Objects.requireNonNull(byIndexStringParameterAdder);

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

            if (byIndexStringAdderParameterPredicate == null || byIndexStringAdderParameterPredicate.test(byIndex, index, predicateParameter)) {

                if (sb.length() > prefixLength) {

                    sb.append(',');
                }

                byIndexStringParameterAdder.addString(byIndex, index, sb, adderParameter);
            }
        }

        sb.append(']');
    }
}
