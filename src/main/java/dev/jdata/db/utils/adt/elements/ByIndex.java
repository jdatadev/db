package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.jdata.db.utils.adt.elements.IElements.IElementEqualityTester;
import dev.jdata.db.utils.adt.strings.StringBuilders;
import dev.jdata.db.utils.checks.Checks;

public class ByIndex {

    @FunctionalInterface
    public interface IByIndexElementEqualityTester<T, P1, P2> extends IElementEqualityTester<T, P1, P2> {

    }

    @FunctionalInterface
    public interface IByIndexGetter<T, U> {

        T get(U byIndex, long index);
    }

    @FunctionalInterface
    public interface IByIndexSetter<T, U> {

       void set(U byIndex, long index, T value);
    }

    public static <T, U> boolean containsInstance(U byIndex, long byIndexLength, T instance, IByIndexGetter<T, U> getter,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(instance);

        return findAtMostOneIndex(byIndex, byIndexLength, 0L, byIndexLength, instance, getter, (e, p) -> e == p, exceptionSupplier) != -1;
    }

    public static <T, U> boolean containsInstance(U byIndex, long byIndexLength, T instance, long startIndex, long numElements, IByIndexGetter<T, U> getter,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(instance);

        return findAtMostOneIndex(byIndex, byIndexLength, startIndex, numElements, instance, (b, i) -> getter.get(b, i), (e, p) -> e == p, exceptionSupplier) != -1;
    }

    public static <T, U, P> boolean contains(U byIndex, long byIndexLength, P parameter, IByIndexGetter<T, U> getter, BiPredicate<T, P> predicate,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return findAtMostOneIndex(byIndex, byIndexLength, 0L, byIndexLength, parameter, getter, predicate, exceptionSupplier) != -1;
    }

    public static <T, U, P> boolean contains(U byIndex, long byIndexLength, long startIndex, long numElements, P parameter, IByIndexGetter<T, U> getter,
            BiPredicate<T, P> predicate, Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return findAtMostOneIndex(byIndex, byIndexLength, startIndex, numElements, parameter, getter, predicate, exceptionSupplier) != -1;
    }

    public static <T, U, P> long findAtMostOneIndex(U byIndex, long byIndexLength, P parameter, IByIndexGetter<T, U> getter, BiPredicate<T, P> predicate,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(byIndex);
        Checks.isLengthAboveOrAtZero(byIndexLength);
        Objects.requireNonNull(getter);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(exceptionSupplier);

        return findAtMostOneIndex(byIndex, byIndexLength, 0, byIndexLength, parameter, getter, predicate, exceptionSupplier);
    }

    public static <T, U, P> long findAtMostOneIndex(U byIndex, long byIndexLength, long startIndex, long numElements, P parameter, IByIndexGetter<T, U> getter,
            BiPredicate<T, P> predicate, Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(byIndex);
        Checks.isLengthAboveOrAtZero(byIndexLength);

        final long endIndex;

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

        long foundIndex = -1L;

        for (long i = startIndex; i < endIndex; ++ i) {

            final T element = getter.get(byIndex, i);

            if (predicate.test(element, parameter)) {

                if (foundIndex != -1L) {

                    throw new IllegalStateException();
                }

                foundIndex = i;
            }
        }

        return foundIndex;
    }

    public static <T, U, V, R> void map(U byIndex, long numElements, IByIndexGetter<T, U> getter, Function<T, R> mapper, IByIndexSetter<R, V> setter, V dst) {

        Objects.requireNonNull(byIndex);
        Checks.isNumElements(numElements);
        Objects.requireNonNull(getter);
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(setter);
        Objects.requireNonNull(dst);

        for (long i = 0L; i < numElements; ++ i) {

            final T value = getter.get(byIndex, i);

            final R mapped = mapper.apply(value);

            setter.set(dst, i, mapped);
        }
    }

    @FunctionalInterface
    public interface IByIndexEqualityTester<T, P1, P2, DELEGATE> {

        boolean equals(T byIndex1, long startIndex1, P1 parameter1, T byIndex2, long startIndex2, P2 parameter2, DELEGATE delegate);
    }

    public static <T, P1, P2, DELEGATE> boolean equals(T byIndex1, long startIndex1, P1 parameter1, T byIndex2, long startIndex2, P2 parameter2, long numElements,
            DELEGATE delegate, IByIndexEqualityTester<T, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(byIndex1);
        Objects.requireNonNull(byIndex2);
        Checks.isAboveZero(numElements);

        boolean equals = true;

        for (long i = 0L; i < numElements; ++ i) {

            if (!equalityTester.equals(byIndex1, startIndex1 + i, parameter1, byIndex2, startIndex2 + i, parameter2, delegate)) {

                equals = false;
                break;
            }
        }

        return equals;
    }

    @FunctionalInterface
    public interface IntByIndexStringAdderPredicate<T> {

        boolean test(T instance, int index);
    }

    @FunctionalInterface
    public interface IntByIndexStringAdder<T> {

        void addString(T instance, int index, StringBuilder sb);
    }

    @FunctionalInterface
    public interface ByIndexStringAdderPredicate<T> {

        boolean test(T instance, long index);
    }

    @FunctionalInterface
    public interface ByIndexStringAdder<T> {

        void addString(T instance, long index, StringBuilder sb);
    }

    public static <T> String closureOrConstantToString(T byIndex, long startIndex, long numElements, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdder<T> byIndexStringAdder) {

        return closureOrConstantToString(byIndex, startIndex, numElements, prefixAdder, null, (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    public static <T> String closureOrConstantToString(T byIndex, long startIndex, long numElements, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, ByIndexStringAdder<T> byIndexStringAdder) {

        final StringBuilder sb = new StringBuilder();

        closureOrConstantToString(byIndex, startIndex, numElements, sb, prefixAdder,
                byIndexStringAdderPredicate != null ? (a, i) -> byIndexStringAdderPredicate.test(a, (int)i) : null, (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));

        return sb.toString();
    }

    public static <T> void closureOrConstantToString(T byIndex, int startIndex, int numElements, StringBuilder sb, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, ByIndexStringAdder<T> byIndexStringAdder) {

        closureOrConstantToString(byIndex, (long)startIndex, (long)numElements, sb, prefixAdder, (a, i) -> byIndexStringAdderPredicate.test(a, (int)i),
                (a, i, b) -> byIndexStringAdder.addString(a, (int)i, b));
    }

    public static <T> String closureOrConstantLargeToString(T byIndex, long startIndex, long numElements, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdder<T> byIndexStringAdder) {

        return closureOrConstantToString(byIndex, startIndex, numElements, prefixAdder, null, byIndexStringAdder);
    }

    public static <T> void closureOrConstantToString(T byIndex, long startIndex, long numElements, StringBuilder sb, BiConsumer<T, StringBuilder> prefixAdder,
            ByIndexStringAdderPredicate<T> byIndexStringAdderPredicate, ByIndexStringAdder<T> byIndexStringAdder) {

        toString(byIndex, startIndex, numElements, sb, byIndexStringAdderPredicate, byIndexStringAdder, prefixAdder,
                byIndexStringAdderPredicate != null ? (instance, index, parameter) -> parameter.test(instance, index) : null,
                (instance, index, b, parameter) -> parameter.addString(instance, index, b));
    }

    @FunctionalInterface
    public interface ByIndexStringAdderParameterPredicate<T, P> {

        boolean test(T instance, long index, P parameter);
    }

    @FunctionalInterface
    public interface ByIndexStringParameterAdder<T, P> {

        void addString(T instance, long index, StringBuilder sb, P parameter);
    }

    public static <T, U, V> void toString(T byIndex, long startIndex, long numElements, StringBuilder sb, V adderParameter,
            ByIndexStringParameterAdder<T, V> byIndexStringParameterAdder) {

        toString(byIndex, startIndex, numElements, sb, null, adderParameter, null, null, byIndexStringParameterAdder);
    }

    public static <T, U, V> void toString(T byIndex, long startIndex, long numElements, StringBuilder sb, U predicateParameter, V adderParameter,
            BiConsumer<T, StringBuilder> prefixAdder, ByIndexStringAdderParameterPredicate<T, U> byIndexStringAdderParameterPredicate,
            ByIndexStringParameterAdder<T, V> byIndexStringParameterAdder) {

        Objects.requireNonNull(byIndex);
        Checks.isIndex(startIndex);
        Checks.isNumElements(numElements);
        Objects.requireNonNull(byIndexStringParameterAdder);

        if (prefixAdder != null) {

            prefixAdder.accept(byIndex, sb);

            if (!StringBuilders.isEmpty(sb)) {

                sb.append(' ');
            }
        }

        sb.append('[');

        final int prefixLength = sb.length();

        for (long i = 0L; i < numElements; ++ i) {

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
