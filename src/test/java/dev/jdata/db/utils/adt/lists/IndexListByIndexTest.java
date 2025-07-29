package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;

import dev.jdata.db.utils.adt.elements.BaseByIndexTest;
import dev.jdata.db.utils.scalars.Integers;

public final class IndexListByIndexTest extends BaseByIndexTest {

    public IndexListByIndexTest() {
        super(IndexOutOfBoundsException.class);
    }

    @Override
    protected <T, R> R[] map(T[] array, IntFunction<R[]> createMappedArray, Function<T, R> mapper) {

        final LongFunction<IIndexListBuildable<R, ?, ?>> createIndexListAddable = l -> IndexList.createBuilder(createMappedArray);

        return makeList(array).map(createIndexListAddable, mapper).toArray(createMappedArray);
    }

    @Override
    protected <T> boolean equals(T[] array1, int startIndex1, T[] array2, int startIndex2, int numElements) {

        return makeList(array1).equals(startIndex1, makeList(array2), startIndex2, numElements);
    }

    @Override
    protected <T, P> boolean equals(T[] array1, P parameter1, T[] array2, P parameter2, IByIndexTestEqualityTester<T, P> byIndexEqualityTester) {

        final IIndexListGetters<T> list1 = makeList(array1);
        final IIndexListGetters<T> list2 = makeList(array2);

        return list1.equals(parameter1, list2, parameter2,
                byIndexEqualityTester != null
                        ? (e1, p1, e2, p2) -> byIndexEqualityTester.equals(e1, p1, e2, p2)
                        : null);
    }

    @Override
    protected <T, P> boolean equals(T[] array1, int startIndex1, P parameter1, T[] array2, int startIndex2, P parameter2, int numElements,
            IByIndexTestEqualityTester<T, P> byIndexEqualityTester) {

        final IIndexListGetters<T> list1 = makeList(array1);
        final IIndexListGetters<T> list2 = makeList(array2);

        return list1.equals(startIndex1, parameter1, list2, startIndex2, parameter2, numElements,
                byIndexEqualityTester != null
                        ? (e1, p1, e2, p2) -> byIndexEqualityTester.equals(e1, p1, e2, p2)
                        : null);
    }

    @Override
    protected <T> boolean containsInstance(T[] array, T instance) {

        return makeList(array).containsInstance(instance);
    }

    @Override
    protected <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance) {

        return makeList(array).containsInstance(instance, startIndex, numElements);
    }

    @Override
    protected <T, P> int findAtMostOneIndex(T[] array, P parameter, BiPredicate<T, P> predicate) {

        return Integers.checkLongToInt(makeList(array).findAtMostOneIndex(parameter, predicate));
    }

    @Override
    protected <T> int closureOrConstantFindAtMostOneIndex(T[] array, Predicate<T> predicate) {

        return Integers.checkLongToInt(makeList(array).closureOrConstantFindAtMostOneIndex(predicate));
    }

    @Override
    protected <T, P> int findAtMostOneIndexInRange(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate) {

        return Integers.checkLongToInt(makeList(array).findAtMostOneIndexInRange(startIndex, numElements, parameter, predicate));
    }

    @Override
    protected <T> int closureOrConstantFindAtMostOneIndexInRange(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return Integers.checkLongToInt(makeList(array).closureOrConstantFindAtMostOneIndexInRange(startIndex, numElements, predicate));
    }

    private static <T> IIndexListGetters<T> makeList(T[] array) {

        return IndexList.of(array);
    }
}
