package dev.jdata.db.utils.adt.elements;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseElementsAggregatesTest<T> extends BaseElementsTest {

    protected abstract T createTestElements(Integer[] elementsToAdd);

    protected abstract long countWithClosure(T elements, Predicate<Integer> predicate);
    protected abstract <P> long count(T elements, P parameter, BiPredicate<Integer, P> predicate);

    protected abstract int maxInt(T elements, int defaultValue, ToIntFunction<Integer> mapper);
    protected abstract long maxLong(T elements, long defaultValue, ToLongFunction<Integer> mapper);

    private final T createElements(int ... elements) {

        return createTestElements(makeElementsToAdd(elements));
    }

    @Test
    @Category(UnitTest.class)
    public final void testCount() {

        final Object parameterObject = new Object();

        assertThatThrownBy(() -> count(null, parameterObject, (e, p) -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> count(createElements(1, 2, 3), parameterObject, null)).isInstanceOf(NullPointerException.class);

        count(createElements(), parameterObject, (e, p) -> {

            assertThat(p).isSameAs(parameterObject);

            return true;
        });

        checkCount(null, this::count);
    }

    @Test
    @Category(UnitTest.class)
    public final void testCountWithClosure() {

        assertThatThrownBy(() -> countWithClosure(null, e -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> countWithClosure(createElements(1, 2, 3), null)).isInstanceOf(NullPointerException.class);

        checkCount(null, (elements, parameter, predicate) -> countWithClosure(elements, e -> predicate.test(e, null)));
    }

    @FunctionalInterface
    private interface CountFunction<T, P> {

        long apply(T elements, P parameter, BiPredicate<Integer, P> predicate);
    }

    private <P> void checkCount(P parameter, CountFunction<T, P> countFunction) {

        assertThat(countFunction.apply(createElements(), parameter, (e, p) -> true)).isEqualTo(0L);

        assertThat(countFunction.apply(createElements(0),             parameter, (e, p) -> false)).isEqualTo(0);
        assertThat(countFunction.apply(createElements(0),             parameter, (e, p) -> true)).isEqualTo(1);
        assertThat(countFunction.apply(createElements(0, 1),          parameter, (e, p) -> false)).isEqualTo(0);
        assertThat(countFunction.apply(createElements(0, 1),          parameter, (e, p) -> true)).isEqualTo(2);
        assertThat(countFunction.apply(createElements(0, 1),          parameter, (e, p) -> e < 1)).isEqualTo(1);
        assertThat(countFunction.apply(createElements(1, 2 ,0),       parameter, (e, p) -> false)).isEqualTo(0);
        assertThat(countFunction.apply(createElements(1, 2 ,0),       parameter, (e, p) -> true)).isEqualTo(3);
        assertThat(countFunction.apply(createElements(1, 2 ,0),       parameter, (e, p) -> e < 2)).isEqualTo(2);
        assertThat(countFunction.apply(createElements(1, 0, 1, -2),   parameter, (e, p) -> e != 0)).isEqualTo(3);
    }

    @Test
    @Category(UnitTest.class)
    public final void testMaxInt() {

        checkMax((T e, int d, ToIntFunction<Integer> m) -> maxInt(e, d, m));
    }

    @Test
    @Category(UnitTest.class)
    public final void testMaxLong() {

        checkMax((T e, int d, ToIntFunction<Integer> m) -> Integers.checkLongToInt(maxLong(e, d, m != null ? o -> Integers.checkLongToInt(m.applyAsInt(o)) : null)));
    }

    @FunctionalInterface
    interface Aggregator<T> {

        int aggregate(T elements, int defaultValue, ToIntFunction<Integer> mapper);
    }

    private <E> void checkMax(Aggregator<T> aggregator) {

        assertThatThrownBy(() -> aggregator.aggregate(null, 0, Integer::intValue)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> aggregator.aggregate(createElements(1, 2, 3), 0, null)).isInstanceOf(NullPointerException.class);

        assertThat(aggregator.aggregate(createElements(), 0,   Integer::intValue)).isEqualTo(0);
        assertThat(aggregator.aggregate(createElements(), 123, Integer::intValue)).isEqualTo(123);

        assertThat(aggregator.aggregate(createElements(0),                  123, Integer::intValue)).isEqualTo(0);
        assertThat(aggregator.aggregate(createElements(0, 1),               123, Integer::intValue)).isEqualTo(1);
        assertThat(aggregator.aggregate(createElements(1, 2 ,0),            123, Integer::intValue)).isEqualTo(2);
        assertThat(aggregator.aggregate(createElements(1, 0, -2),           123, Integer::intValue)).isEqualTo(1);
        assertThat(aggregator.aggregate(createElements(1, 0, 1, -2),        123, Integer::intValue)).isEqualTo(1);
        assertThat(aggregator.aggregate(createElements(-1, 0, -1, -2),      123, Integer::intValue)).isEqualTo(0);
        assertThat(aggregator.aggregate(createElements(-1, -3, -1, -2),     123, Integer::intValue)).isEqualTo(-1);

        assertThat(aggregator.aggregate(createElements(2, 3, 1), 123, i -> i + 1)).isEqualTo(4);
    }

    private static Integer[] makeElementsToAdd(int ... elements) {

        return Array.boxed(elements);
    }
}
