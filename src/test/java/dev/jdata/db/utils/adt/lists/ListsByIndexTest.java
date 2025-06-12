package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import dev.jdata.db.utils.adt.elements.BaseByIndexTest;

public final class ListsByIndexTest extends BaseByIndexTest {

    public ListsByIndexTest() {
        super(IndexOutOfBoundsException.class);
    }

    @Override
    protected <T, R> R[] map(T[] array, IntFunction<R[]> createMappedArray, Function<T, R> mapper) {

        return Lists.map(Arrays.asList(array), mapper).toArray(createMappedArray);
    }

    @Override
    protected <T, P> boolean equals(T[] array1, P parameter1, T[] array2, P parameter2, IByIndexTestEqualityTester<T, P> byIndexEqualityTester) {

        return Lists.equals(Arrays.asList(array1), parameter1, Arrays.asList(array2), parameter2,
                byIndexEqualityTester != null
                        ? (e1, p1, e2, p2) -> byIndexEqualityTester.equals(e1, p1, e2, p2)
                        : null);
    }

    @Override
    protected <T, P> boolean equals(T[] array1, int startIndex1, P parameter1, T[] array2, int startIndex2, P parameter2, int numElements,
            IByIndexTestEqualityTester<T, P> byIndexEqualityTester) {

        return Lists.equals(Arrays.asList(array1), startIndex1, parameter1, Arrays.asList(array2), startIndex2, parameter2, numElements,
                byIndexEqualityTester != null
                        ? (e1, p1, e2, p2) -> byIndexEqualityTester.equals(e1, p1, e2, p2)
                        : null);
    }

    @Override
    protected <T> boolean containsInstance(T[] array, T instance) {

        return Lists.containsInstance(Arrays.asList(array), instance);
    }

    @Override
    protected <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance) {

        return Lists.containsInstance(Arrays.asList(array), instance, startIndex, numElements);
    }

    @Override
    protected <T, P> int findIndex(T[] array, P parameter, BiPredicate<T, P> predicate) {

        return Lists.findIndex(Arrays.asList(array), parameter, predicate);
    }

    @Override
    protected <T> int closureOrConstantFindIndex(T[] array, Predicate<T> predicate) {

        return Lists.closureOrConstantFindIndex(Arrays.asList(array), predicate);
    }

    @Override
    protected <T, P> int findIndexInRange(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return Lists.findIndex(Arrays.asList(array), array.length, startIndex, numElements, parameter, predicate);
    }

    @Override
    protected <T> int closureOrConstantFindIndexInRange(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return Lists.closureOrConstantFindIndex(Arrays.asList(array), startIndex, numElements, predicate);
    }
}
