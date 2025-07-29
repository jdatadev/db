package dev.jdata.db.utils.adt.arrays;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.elements.BaseElementsAggregatesTest;

public final class ArrayAggregatesTest extends BaseElementsAggregatesTest<Integer[]> {

    @Override
    protected Integer[] createTestElements(Integer[] elementsToAdd) {

        return elementsToAdd;
    }

    @Override
    protected long countWithClosure(Integer[] elements, Predicate<Integer> predicate) {

        return Array.closureOrConstantCount(elements, predicate);
    }

    @Override
    protected <P> long count(Integer[] elements, P parameter, BiPredicate<Integer, P> predicate) {

        return Array.count(elements, parameter, predicate);
    }

    @Override
    protected int maxInt(Integer[] elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return Array.maxInt(elements, defaultValue, mapper);
    }

    @Override
    protected long maxLong(Integer[] elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return Array.maxLong(elements, defaultValue, mapper);
    }
}
