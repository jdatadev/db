package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
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
    protected <T> boolean containsInstance(T[] array, T instance) {

        return Lists.containsInstance(Arrays.asList(array), instance);
    }

    @Override
    protected <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance) {

        return Lists.containsInstance(Arrays.asList(array), startIndex, numElements, instance);
    }

    @Override
    protected <T> int findIndex(T[] array, Predicate<T> predicate) {

        return Lists.findIndexWithClosureAllocation(Arrays.asList(array), predicate);
    }

    @Override
    protected <T> int findIndexRange(T[] array, int startIndex, int numElements, Predicate<T> predicate) {

        return Lists.findIndexWithClosureAllocation(Arrays.asList(array), startIndex, numElements, predicate);
    }
}
