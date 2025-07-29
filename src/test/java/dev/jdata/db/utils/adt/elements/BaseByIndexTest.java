package dev.jdata.db.utils.adt.elements;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public abstract class BaseByIndexTest extends BaseElementsTest {

    protected abstract <T, R> R[] map(T[] array, IntFunction<R[]> createMappedArray, Function<T, R> mapper);

    protected abstract <T> boolean equals(T[] array1, int startIndex1, T[] array2, int startIndex2, int numElements);

    @FunctionalInterface
    public interface IByIndexTestEqualityTester<T, P> {

        boolean equals(T element1, P parameter1, T element2, P parameter2);
    }

    protected abstract <T, P> boolean equals(T[] array1, P parameter1, T[] array2, P parameter2, IByIndexTestEqualityTester<T, P> byIndexEqualityTester);

    protected abstract <T, P> boolean equals(T[] array1, int startIndex1, P parameter1, T[] array2, int startIndex2, P parameter2, int numElements,
            IByIndexTestEqualityTester<T, P> byIndexEqualityTester);

    protected abstract <T> boolean containsInstance(T[] array, T instance);
    protected abstract <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance);
    protected abstract <T, P> int findAtMostOneIndex(T[] array, P parameter, BiPredicate<T, P> predicate);
    protected abstract <T> int closureOrConstantFindAtMostOneIndex(T[] array, Predicate<T> predicate);
    protected abstract <T, P> int findAtMostOneIndexInRange(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate);
    protected abstract <T> int closureOrConstantFindAtMostOneIndexInRange(T[] array, int startIndex, int numElements, Predicate<T> predicate);

    private final Class<? extends IndexOutOfBoundsException> indexOutOfBoundsExceptionClass;

    protected BaseByIndexTest(Class<? extends IndexOutOfBoundsException> indexOutOfBoundsExceptionClass) {

        this.indexOutOfBoundsExceptionClass = Objects.requireNonNull(indexOutOfBoundsExceptionClass);
    }

    @Test
    @Category(UnitTest.class)
    public final void testMap() {

        assertThatThrownBy(() -> map(null, String[]::new, e -> null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> map(create(1), String[]::new, null)).isInstanceOf(NullPointerException.class);

        final String[] oneMapped = map(create(1), String[]::new, String::valueOf);
        assertThat(oneMapped).containsExactly("1");

        final String[] threeMapped = map(create(1, 2, 3), String[]::new, String::valueOf);
        assertThat(threeMapped).containsExactly("1", "2", "3");
    }
    @Test
    @Category(UnitTest.class)
    public final void testEquals() {

        final IByIndexTestEqualityTester<Integer, Void> byIndexEqualityTester = (e1, p1, e2, p2) -> true;

        assertThatThrownBy(() -> equals(null, null, create(1), null, byIndexEqualityTester)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> equals(create(1), null, null, null, byIndexEqualityTester)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> equals(create(1), null, create(1), null, null)).isInstanceOf(NullPointerException.class);

        checkEquals(create(1), create(1), true);

        checkEquals(create(1, 2, 3), create(), false);
        checkEquals(create(1, 2, 3), create(1), false);
        checkEquals(create(1, 2, 3), create(1, 2), false);

        checkEquals(create(), create(1, 2, 3), false);
        checkEquals(create(1), create(1, 2, 3), false);
        checkEquals(create(1, 2), create(1, 2, 3), false);

        checkEquals(create(1, 2, 3), create(1, 2, 3), true);
    }

    private <T> void checkEquals(T[] array1, T[] array2, boolean expectedResult) {

        final Object parameter1 = new Object();
        final Object parameter2 = new Object();

        assertThat(equals(array1, parameter1, array2, parameter2, (e1, p1, e2, p2) -> {

            assertThat(p1).isSameAs(parameter1);
            assertThat(p2).isSameAs(parameter2);

            return e1.equals(e2);

        })).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public final void testEqualsRange() {

        final IByIndexTestEqualityTester<Integer, Void> byIndexEqualityTester = (e1, p1, e2, p2) -> true;

        assertThatThrownBy(() -> equals(null, 0, null, create(1), 0, null, 1, byIndexEqualityTester)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> equals(create(1), 0, null, null, 0, null, 1, byIndexEqualityTester)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> equals(create(1), 0, null, create(1), 0, null, 1, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> equals(create(1), -1, null, create(1), 0, null, 1, byIndexEqualityTester)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> equals(create(1), 1, null, create(1), 0, null, 1, byIndexEqualityTester)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> equals(create(1), 0, null, create(1), -1, null, 1, byIndexEqualityTester)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> equals(create(1), 0, null, create(1), 1, null, 1, byIndexEqualityTester)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> equals(create(1), 0, null, create(1), 0, null, 0, byIndexEqualityTester)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> equals(create(1), 0, null, create(1), 0, null, 2, byIndexEqualityTester)).isInstanceOf(indexOutOfBoundsExceptionClass);

        assertThat(equals(create(1), 0, null, create(1), 0, null, 1, byIndexEqualityTester)).isTrue();

        checkEquals(create(1), 0, create(1), 0, 1, true);
        checkEquals(create(1, 2, 3), 0, create(1, 2, 3), 0, 1, true);
        checkEquals(create(1, 2, 3), 0, create(1, 2, 3), 0, 2, true);
        checkEquals(create(1, 2, 3), 0, create(1, 2, 3), 0, 3, true);

        checkEquals(create(1, 2, 3), 0, create(1, 2, 3), 1, 1, false);
        checkEquals(create(1, 2, 3), 0, create(1, 2, 3), 1, 2, false);

        checkEquals(create(1, 2, 3), 1, create(1, 2, 3), 0, 1, false);
        checkEquals(create(1, 2, 3), 2, create(1, 2, 3), 0, 2, false);
    }

    private <T> void checkEquals(T[] array1, int startIndex1, T[] array2, int startIndex2, int numElements, boolean expectedResult) {

        final Object parameter1 = new Object();
        final Object parameter2 = new Object();

        assertThat(equals(array1, startIndex1, parameter1, array2, startIndex2, parameter2, numElements, (e1, p1, e2, p2) -> {

            assertThat(p1).isSameAs(parameter1);
            assertThat(p2).isSameAs(parameter2);

            return e1.equals(e2);

        })).isEqualTo(expectedResult);
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsInstance() {

        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertThatThrownBy(() -> containsInstance(null, object1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> containsInstance(new Object[0], null)).isInstanceOf(NullPointerException.class);

        assertThat(containsInstance(new Object[0], object1)).isFalse();

        assertThat(containsInstance(new Object[] { object1 }, object1)).isTrue();
        assertThat(containsInstance(new Object[] { object1 }, object2)).isFalse();

        assertThat(containsInstance(new Object[] { object1, object2 }, object1)).isTrue();
        assertThat(containsInstance(new Object[] { object1, object2 }, object2)).isTrue();
        assertThat(containsInstance(new Object[] { object1, object2 }, object3)).isFalse();

        assertThat(containsInstance(new Object[] { object1, object2, object3 }, object1)).isTrue();
        assertThat(containsInstance(new Object[] { object1, object2, object3 }, object2)).isTrue();
        assertThat(containsInstance(new Object[] { object1, object2, object3 }, object3)).isTrue();
        assertThat(containsInstance(new Object[] { object1, object2, object3 }, object4)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsInstanceRange() {

        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertThatThrownBy(() -> containsInstanceRange(null, 0, 0, object1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], -1, 0, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], 0, 1, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], 1, 0, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[] { object1 }, 1, 0, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[] { object1 }, 0, 2, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], 0, 0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> containsInstanceRange(new Object[] { object1 }, 0, 1, null)).isInstanceOf(NullPointerException.class);

        assertThat(containsInstanceRange(new Object[0], 0, 0, object1)).isFalse();

        assertThat(containsInstanceRange(new Object[] { object1 }, 0, 0, object1)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1 }, 0, 1, object1)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1 }, 0, 1, object2)).isFalse();

        assertThat(containsInstanceRange(new Object[] { object1, object2 }, 0, 1, object1)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2 }, 1, 1, object1)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2 }, 0, 2, object1)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2 }, 0, 1, object2)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2 }, 1, 1, object2)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2 }, 0, 2, object2)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2 }, 0, 2, object3)).isFalse();

        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 1, object1)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 2, object1)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 3, object1)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 1, 1, object1)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 1, 2, object1)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 2, 1, object1)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 1, object2)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 2, object2)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 3, object2)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 1, 1, object2)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 1, 2, object2)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 2, 1, object2)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 1, object3)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 2, object3)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 3, object3)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 1, 1, object3)).isFalse();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 1, 2, object3)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 2, 1, object3)).isTrue();
        assertThat(containsInstanceRange(new Object[] { object1, object2, object3 }, 0, 3, object4)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testFindAtMostOneIndex() {

        final Object parameter = new Object();

        assertThatThrownBy(() -> findAtMostOneIndex(null, parameter, (e, p) -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> findAtMostOneIndex(new Integer[] { 1, 2, 3 }, parameter, null)).isInstanceOf(NullPointerException.class);

        assertThat(findAtMostOneIndex(new Integer[] { 123, 234 }, parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return e == 234;
        }))
        .isEqualTo(1);

        checkFindIndex((a, p) -> findAtMostOneIndex(a, parameter, p));
    }

    @Test
    @Category(UnitTest.class)
    public final void testClosureOrConstantFindAtMostOneIndex() {

        assertThatThrownBy(() -> closureOrConstantFindAtMostOneIndex(null, e -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> closureOrConstantFindAtMostOneIndex(new Integer[] { 1, 2, 3 }, null)).isInstanceOf(NullPointerException.class);

        checkFindIndex((a, p) -> closureOrConstantFindAtMostOneIndex(a, e -> p.test(e, null)));
    }

    @FunctionalInterface
    private interface FindIndex<T, P> {

        int apply(T[] array, BiPredicate<T, P> predicate);
    }

    private static <P> void checkFindIndex(FindIndex<Integer, P> findIndex) {

        assertThatThrownBy(() -> findIndex.apply(new Integer[] { 0, 1, 2, 1 }, (e, p) -> e == 1)).isInstanceOf(IllegalStateException.class);

        assertThat(findIndex.apply(new Integer[0], (e, p) -> true)).isEqualTo(-1);

        assertThat(findIndex.apply(new Integer[] { 0 },                 (e, p) -> e == -1)).isEqualTo(-1);
        assertThat(findIndex.apply(new Integer[] { 0 },                 (e, p) -> e == 0)).isEqualTo(0);
        assertThat(findIndex.apply(new Integer[] { 0 },                 (e, p) -> e == 1)).isEqualTo(-1);
        assertThat(findIndex.apply(new Integer[] { 0, 1 },              (e, p) -> e == -1)).isEqualTo(-1);
        assertThat(findIndex.apply(new Integer[] { 0, 1 },              (e, p) -> e == 0)).isEqualTo(0);
        assertThat(findIndex.apply(new Integer[] { 0, 1 },              (e, p) -> e == 1)).isEqualTo(1);
        assertThat(findIndex.apply(new Integer[] { 0, 1 },              (e, p) -> e == 2)).isEqualTo(-1);
        assertThat(findIndex.apply(new Integer[] { 1, 2 ,0 },           (e, p) -> e == -1)).isEqualTo(-1);
        assertThat(findIndex.apply(new Integer[] { 1, 2 ,0 },           (e, p) -> e == 1)).isEqualTo(0);
        assertThat(findIndex.apply(new Integer[] { 1, 2 ,0 },           (e, p) -> e == 2)).isEqualTo(1);
        assertThat(findIndex.apply(new Integer[] { 1, 2 ,0 },           (e, p) -> e == 0)).isEqualTo(2);
        assertThat(findIndex.apply(new Integer[] { 1, 2 ,0 },           (e, p) -> e == 3)).isEqualTo(-1);
        assertThat(findIndex.apply(new Integer[] { 1, 0, -2, 1 },       (e, p) -> e == 0)).isEqualTo(1);
        assertThat(findIndex.apply(new Integer[] { -1, 0, -3, -2, -1 }, (e, p) -> e == -3)).isEqualTo(2);
        assertThat(findIndex.apply(new Integer[] { 1, null ,0 },        (e, p) -> e == null)).isEqualTo(1);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFindAtMostOneIndexInRange() {

        final Object object1 = new Object();

        final Object parameter = new Object();

        assertThatThrownBy(() -> findAtMostOneIndexInRange(null, 0, 0, parameter, (e, p) -> e == object1)).isInstanceOf(NullPointerException.class);

        final FindIndexInRange<Object, Object> findIndexInRange = (a, s, n, p) -> findAtMostOneIndexInRange(a, s, n, parameter, p);

        checkFindAtMostOneIndexInRangeExceptions(indexOutOfBoundsExceptionClass, findIndexInRange);

        assertThatThrownBy(() -> findAtMostOneIndexInRange(new Object[0], 0, 0, parameter, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> findAtMostOneIndexInRange(new Object[] { object1 }, 0, 1, parameter, null)).isInstanceOf(NullPointerException.class);

        final Integer[] array = new Integer[] { 123, 234 };

        assertThat(findAtMostOneIndexInRange(array, 0, array.length, parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return e == 234;
        }))
        .isSameAs(1);

        checkFindAtMostOneIndexInRange((a, s, n, p) -> findAtMostOneIndexInRange(a, s, n, parameter, p));
    }

    @Test
    @Category(UnitTest.class)
    public final void testClosureOrConstantFindAtMostOneIndexInRange() {

        final Object object1 = new Object();

        assertThatThrownBy(() -> closureOrConstantFindAtMostOneIndexInRange(null, 0, 0, e -> e == object1)).isInstanceOf(NullPointerException.class);

        final FindIndexInRange<Object, Object> findIndexInRange = (a, s, n, p) -> closureOrConstantFindAtMostOneIndexInRange(a, s, n, e -> p.test(e, null));

        checkFindAtMostOneIndexInRangeExceptions(indexOutOfBoundsExceptionClass, findIndexInRange);

        assertThatThrownBy(() -> closureOrConstantFindAtMostOneIndexInRange(new Object[0], 0, 0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> closureOrConstantFindAtMostOneIndexInRange(new Object[] { object1 }, 0, 1, null)).isInstanceOf(NullPointerException.class);

        checkFindAtMostOneIndexInRange(findIndexInRange);
    }

    @FunctionalInterface
    private interface FindIndexInRange<T, P> {

        int apply(T[] array, int startIndex, int numElements, BiPredicate<T, P> predicate);
    }

    private static <P> void checkFindAtMostOneIndexInRangeExceptions(Class<? extends IndexOutOfBoundsException> indexOutOfBoundsExceptionClass,
            FindIndexInRange<Object, P> findIndexInRange) {

        final Object object1 = new Object();

        assertThatThrownBy(() -> findIndexInRange.apply(new Object[] { 0, object1, 2, object1 }, 0, 4, (e, p) -> e == object1)).isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> findIndexInRange.apply(new Object[0], -1, 0, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[0], 0, 1, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[0], 1, 0, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[] { object1 }, 1, 0, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[] { object1 }, 0, 2, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
    }

    private static <P> void checkFindAtMostOneIndexInRange(FindIndexInRange<Object, P> findIndexInRange) {

        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertThat(findIndexInRange.apply(new Object[0], 0, 0, (e, p) -> e == object1)).isEqualTo(-1);

        assertThat(findIndexInRange.apply(new Object[] { object1 }, 0, 0, (e, p) -> e == object1)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1 }, 0, 1, (e, p) -> e == object1)).isEqualTo(0);
        assertThat(findIndexInRange.apply(new Object[] { object1 }, 0, 1, (e, p) -> e == object2)).isEqualTo(-1);

        assertThat(findIndexInRange.apply(new Object[] { object1, object2 }, 0, 1, (e, p) -> e == object1)).isEqualTo(0);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2 }, 1, 1, (e, p) -> e == object1)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2 }, 0, 2, (e, p) -> e == object1)).isEqualTo(0);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2 }, 0, 1, (e, p) -> e == object2)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2 }, 1, 1, (e, p) -> e == object2)).isEqualTo(1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2 }, 0, 2, (e, p) -> e == object2)).isEqualTo(1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2 }, 0, 2, (e, p) -> e == object3)).isEqualTo(-1);

        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 1, (e, p) -> e == object1)).isEqualTo(0);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 2, (e, p) -> e == object1)).isEqualTo(0);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 3, (e, p) -> e == object1)).isEqualTo(0);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 1, 1, (e, p) -> e == object1)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 1, 2, (e, p) -> e == object1)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 2, 1, (e, p) -> e == object1)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 1, (e, p) -> e == object2)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 2, (e, p) -> e == object2)).isEqualTo(1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 3, (e, p) -> e == object2)).isEqualTo(1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 1, 1, (e, p) -> e == object2)).isEqualTo(1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 1, 2, (e, p) -> e == object2)).isEqualTo(1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 2, 1, (e, p) -> e == object2)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 1, (e, p) -> e == object3)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 2, (e, p) -> e == object3)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 3, (e, p) -> e == object3)).isEqualTo(2);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 1, 1, (e, p) -> e == object3)).isEqualTo(-1);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 1, 2, (e, p) -> e == object3)).isEqualTo(2);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 2, 1, (e, p) -> e == object3)).isEqualTo(2);
        assertThat(findIndexInRange.apply(new Object[] { object1, object2, object3 }, 0, 3, (e, p) -> e == object4)).isEqualTo(-1);
    }

    @FunctionalInterface
    protected interface NumElementsGetter<T> {

        int getNumElements(T byIndex);
    }

    @FunctionalInterface
    protected interface ElementComparator<T> {

        boolean areEqual(T element, int integer);
    }

    protected static <T, U, E> void checkCopyElements(Function<int[], T> create, Function<T, U> copy, ElementGetter<U, E> elementGetter,
            NumElementsGetter<U> numElementsGetter) {

        checkCopyOfElementsByIndex(create, copy, elementGetter, numElementsGetter, (e, i) -> e.equals(i));
    }

    protected static <T, U, E> void checkCopyOfElementsByIndex(Function<int[], T> create, Function<T, U> copy, ElementGetter<U, E> elementGetter,
            NumElementsGetter<U> numElementsGetter, ElementComparator<E> elementComparator) {

        Objects.requireNonNull(create);
        Objects.requireNonNull(copy);

        assertThatThrownBy(() -> copy.apply(null)).isInstanceOf(NullPointerException.class);

        checkCopyOfElementsByIndexNonNull(create, copy, elementGetter, numElementsGetter, elementComparator);
    }

    protected static <T, U, E> void checkSafeCopyOfElementsByIndex(Function<int[], T> create, Function<T, U> copy, ElementGetter<U, E> elementGetter,
            NumElementsGetter<U> numElementsGetter, ElementComparator<E> elementComparator) {

        Objects.requireNonNull(create);
        Objects.requireNonNull(copy);

        assertThat(copy.apply(null)).isNull();

        checkCopyOfElementsByIndexNonNull(create, copy, elementGetter, numElementsGetter, elementComparator);
    }

    private static <T, U, E> void checkCopyOfElementsByIndexNonNull(Function<int[], T> create, Function<T, U> copy, ElementGetter<U, E> elementGetter,
            NumElementsGetter<U> numElementsGetter, ElementComparator<E> elementComparator) {

        checkCopyIntElements(create, copy, elementGetter, numElementsGetter, elementComparator);
        checkCopyIntElements(create, copy, elementGetter, numElementsGetter, elementComparator, 12);
        checkCopyIntElements(create, copy, elementGetter, numElementsGetter, elementComparator, 12, 23);
        checkCopyIntElements(create, copy, elementGetter, numElementsGetter, elementComparator, 12, 23, 34);
    }

    private static <T, U, E> void checkCopyIntElements(Function<int[], T> create, Function<T, U> copy, ElementGetter<U, E> elementGetter,
            NumElementsGetter<U> numElementsGetter, ElementComparator<E> elementComparator, int ... elements) {

        final T byIndex = create.apply(elements);

        final U c = copy.apply(byIndex);

        assertThat(c).isNotSameAs(byIndex);

        final int numElements = elements.length;

        assertThat(numElementsGetter.getNumElements(c)).isEqualTo(numElements);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(elementComparator.areEqual(elementGetter.getElement(c, i), elements[i])).isTrue();
        }
    }

    private static Integer[] create(Integer ... values) {

        return values;
    }
}
