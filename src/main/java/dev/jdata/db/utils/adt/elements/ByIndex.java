package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Supplier;

import dev.jdata.db.utils.checks.Checks;

public class ByIndex {

    @FunctionalInterface
    public interface ByIndexGetter<T, U> {

        T get(U byIndex, int index);
    }

    @FunctionalInterface
    public interface ByIndexPredicate<T> {

        boolean test(T byIndex, int index);
    }

    public static <T, U> boolean containsInstance(U byIndex, int byIndexLength, T instance, ByIndexGetter<T, U> getter, Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return containsInstance(byIndex, byIndexLength, 0, byIndexLength, instance, getter, exceptionSupplier);
    }

    public static <T, U> boolean containsInstance(U byIndex, int byIndexLength, int startIndex, int numElements, T instance, ByIndexGetter<T, U> getter,
            Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        Objects.requireNonNull(instance);

        return findIndex(byIndex, byIndexLength, startIndex, numElements, (b, i) -> getter.get(b, i) == instance, exceptionSupplier) != -1;
    }

    public static <T, U> int findIndex(U byIndex, int byIndexLength, ByIndexPredicate<U> predicate, Supplier<IndexOutOfBoundsException> exceptionSupplier) {

        return byIndexLength != 0 ? findIndex(byIndex, byIndexLength, 0, byIndexLength, predicate, exceptionSupplier) : -1;
    }

    public static <T, U> int findIndex(U byIndex, int byIndexLength, int startIndex, int numElements, ByIndexPredicate<U> predicate,
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

            if (predicate.test(byIndex, i)) {

                foundIndex = i;
                break;
            }
        }

        return foundIndex;
    }
}
