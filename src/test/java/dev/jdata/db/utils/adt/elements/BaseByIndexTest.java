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

    protected abstract <T> boolean containsInstance(T[] array, T instance);
    protected abstract <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance);
    protected abstract <T, P> int findIndex(T[] array, P parameter, BiPredicate<T, P> predicate);
    protected abstract <T> int closureOrConstantFindIndex(T[] array, Predicate<T> predicate);
    protected abstract <T, P> int findIndexInRange(T[] array, int startIndex, int numElements, P parameter, BiPredicate<T, P> predicate);
    protected abstract <T> int closureOrConstantFindIndexInRange(T[] array, int startIndex, int numElements, Predicate<T> predicate);

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
    public final void testFindIndex() {

        final Object parameter = new Object();

        assertThatThrownBy(() -> findIndex(null, parameter, (e, p) -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> findIndex(new Integer[] { 1, 2, 3 }, parameter, null)).isInstanceOf(NullPointerException.class);

        assertThat(findIndex(new Integer[] { 123, 234 }, parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return e == 234;
        }))
        .isEqualTo(1);

        checkFindIndex((a, p) -> findIndex(a, parameter, p));
    }

    @Test
    @Category(UnitTest.class)
    public final void testClosureOrConstantFindIndex() {

        assertThatThrownBy(() -> closureOrConstantFindIndex(null, e -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> closureOrConstantFindIndex(new Integer[] { 1, 2, 3 }, null)).isInstanceOf(NullPointerException.class);

        checkFindIndex((a, p) -> closureOrConstantFindIndex(a, e -> p.test(e, null)));
    }

    @FunctionalInterface
    private interface FindIndex<T, P> {

        int apply(T[] array, BiPredicate<T, P> predicate);
    }

    private static <P> void checkFindIndex(FindIndex<Integer, P> findIndex) {

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
        assertThat(findIndex.apply(new Integer[] { 1, 0, -2, 0 },       (e, p) -> e == 0)).isEqualTo(1);
        assertThat(findIndex.apply(new Integer[] { -1, 0, -3, -3, -1 }, (e, p) -> e == -3)).isEqualTo(2);
        assertThat(findIndex.apply(new Integer[] { 1, null ,0 },        (e, p) -> e == null)).isEqualTo(1);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFindIndexInRange() {

        final Object object1 = new Object();

        final Object parameter = new Object();

        assertThatThrownBy(() -> findIndexInRange(null, 0, 0, parameter, (e, p) -> e == object1)).isInstanceOf(NullPointerException.class);

        final FindIndexInRange<Object, Object> findIndexInRange = (a, s, n, p) -> findIndexInRange(a, s, n, parameter, p);

        checkFindIndexInRangeExceptions(indexOutOfBoundsExceptionClass, findIndexInRange);

        assertThatThrownBy(() -> findIndexInRange(new Object[0], 0, 0, parameter, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> findIndexInRange(new Object[] { object1 }, 0, 1, parameter, null)).isInstanceOf(NullPointerException.class);

        final Integer[] array = new Integer[] { 123, 234 };

        assertThat(findIndexInRange(array, 0, array.length, parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return e == 234;
        }))
        .isSameAs(1);

        checkFindIndexInRange((a, s, n, p) -> findIndexInRange(a, s, n, parameter, p));
    }

    @Test
    @Category(UnitTest.class)
    public final void testClosureOrConstantFindIndexInRange() {

        final Object object1 = new Object();

        assertThatThrownBy(() -> closureOrConstantFindIndexInRange(null, 0, 0, e -> e == object1)).isInstanceOf(NullPointerException.class);

        final FindIndexInRange<Object, Object> findIndexInRange = (a, s, n, p) -> closureOrConstantFindIndexInRange(a, s, n, e -> p.test(e, null));

        checkFindIndexInRangeExceptions(indexOutOfBoundsExceptionClass, findIndexInRange);

        assertThatThrownBy(() -> closureOrConstantFindIndexInRange(new Object[0], 0, 0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> closureOrConstantFindIndexInRange(new Object[] { object1 }, 0, 1, null)).isInstanceOf(NullPointerException.class);

        checkFindIndexInRange(findIndexInRange);
    }

    @FunctionalInterface
    private interface FindIndexInRange<T, P> {

        int apply(T[] array, int startIndex, int numElements, BiPredicate<T, P> predicate);
    }

    private static <P> void checkFindIndexInRangeExceptions(Class<? extends IndexOutOfBoundsException> indexOutOfBoundsExceptionClass,
            FindIndexInRange<Object, P> findIndexInRange) {

        final Object object1 = new Object();

        assertThatThrownBy(() -> findIndexInRange.apply(new Object[0], -1, 0, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[0], 0, 1, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[0], 1, 0, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[] { object1 }, 1, 0, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexInRange.apply(new Object[] { object1 }, 0, 2, (e, p) -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
    }

    private static <P> void checkFindIndexInRange(FindIndexInRange<Object, P> findIndexInRange) {

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
        checkCopyIntElements(create, copy, elementGetter, numElementsGetter, elementComparator, 123);
        checkCopyIntElements(create, copy, elementGetter, numElementsGetter, elementComparator, 123, 234);
        checkCopyIntElements(create, copy, elementGetter, numElementsGetter, elementComparator, 123, 234, 345);
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
