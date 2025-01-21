package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public abstract class BaseByIndexTest extends BaseElementsTest {

    protected abstract <T> boolean containsInstance(T[] array, T instance);
    protected abstract <T> boolean containsInstanceRange(T[] array, int startIndex, int numElements, T instance);
    protected abstract <T> int findIndex(T[] array, Predicate<T> predicate);
    protected abstract <T> int findIndexRange(T[] array, int startIndex, int numElements, Predicate<T> predicate);

    private final Class<? extends IndexOutOfBoundsException> indexOutOfBoundsExceptionClass;

    protected BaseByIndexTest(Class<? extends IndexOutOfBoundsException> indexOutOfBoundsExceptionClass) {

        this.indexOutOfBoundsExceptionClass = Objects.requireNonNull(indexOutOfBoundsExceptionClass);
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
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], 0, 0, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], 0, 1, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], 1, 0, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[] { object1 }, 1, 0, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[] { object1 }, 0, 2, object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> containsInstanceRange(new Object[0], 0, 0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> containsInstanceRange(new Object[] { object1 }, 0, 1, null)).isInstanceOf(NullPointerException.class);

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

        assertThatThrownBy(() -> findIndex(null, e -> true)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> findIndex(new Integer[] { 1, 2, 3 }, null)).isInstanceOf(NullPointerException.class);

        assertThat(findIndex(new Integer[0], e -> true)).isEqualTo(-1);

        assertThat(findIndex(new Integer[] { 0 },                 e -> e == -1)).isEqualTo(-1);
        assertThat(findIndex(new Integer[] { 0 },                 e -> e == 0)).isEqualTo(0);
        assertThat(findIndex(new Integer[] { 0 },                 e -> e == 1)).isEqualTo(-1);
        assertThat(findIndex(new Integer[] { 0, 1 },              e -> e == -1)).isEqualTo(-1);
        assertThat(findIndex(new Integer[] { 0, 1 },              e -> e == 0)).isEqualTo(0);
        assertThat(findIndex(new Integer[] { 0, 1 },              e -> e == 1)).isEqualTo(1);
        assertThat(findIndex(new Integer[] { 0, 1 },              e -> e == 2)).isEqualTo(-1);
        assertThat(findIndex(new Integer[] { 1, 2 ,0 },           e -> e == -1)).isEqualTo(-1);
        assertThat(findIndex(new Integer[] { 1, 2 ,0 },           e -> e == 1)).isEqualTo(0);
        assertThat(findIndex(new Integer[] { 1, 2 ,0 },           e -> e == 2)).isEqualTo(1);
        assertThat(findIndex(new Integer[] { 1, 2 ,0 },           e -> e == 0)).isEqualTo(2);
        assertThat(findIndex(new Integer[] { 1, 2 ,0 },           e -> e == 3)).isEqualTo(-1);
        assertThat(findIndex(new Integer[] { 1, 0, -2, 0 },       e -> e == 0)).isEqualTo(1);
        assertThat(findIndex(new Integer[] { -1, 0, -3, -3, -1 }, e -> e == -3)).isEqualTo(2);
        assertThat(findIndex(new Integer[] { 1, null ,0 },        e -> e == null)).isEqualTo(1);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFindIndexRange() {

        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();
        final Object object4 = new Object();

        assertThatThrownBy(() -> findIndexRange(null, 0, 0, e -> e == object1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> findIndexRange(new Object[0], -1, 0, e -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexRange(new Object[0], 0, 0, e -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexRange(new Object[0], 0, 1, e -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexRange(new Object[0], 1, 0, e -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexRange(new Object[] { object1 }, 1, 0, e -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexRange(new Object[] { object1 }, 0, 2, e -> e == object1)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexRange(new Object[0], 0, 0, null)).isInstanceOf(indexOutOfBoundsExceptionClass);
        assertThatThrownBy(() -> findIndexRange(new Object[] { object1 }, 0, 1, null)).isInstanceOf(NullPointerException.class);

        assertThat(findIndexRange(new Object[] { object1 }, 0, 0, e -> e == object1)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1 }, 0, 1, e -> e == object1)).isEqualTo(0);
        assertThat(findIndexRange(new Object[] { object1 }, 0, 1, e -> e == object2)).isEqualTo(-1);

        assertThat(findIndexRange(new Object[] { object1, object2 }, 0, 1, e -> e == object1)).isEqualTo(0);
        assertThat(findIndexRange(new Object[] { object1, object2 }, 1, 1, e -> e == object1)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2 }, 0, 2, e -> e == object1)).isEqualTo(0);
        assertThat(findIndexRange(new Object[] { object1, object2 }, 0, 1, e -> e == object2)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2 }, 1, 1, e -> e == object2)).isEqualTo(1);
        assertThat(findIndexRange(new Object[] { object1, object2 }, 0, 2, e -> e == object2)).isEqualTo(1);
        assertThat(findIndexRange(new Object[] { object1, object2 }, 0, 2, e -> e == object3)).isEqualTo(-1);

        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 1, e -> e == object1)).isEqualTo(0);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 2, e -> e == object1)).isEqualTo(0);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 3, e -> e == object1)).isEqualTo(0);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 1, 1, e -> e == object1)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 1, 2, e -> e == object1)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 2, 1, e -> e == object1)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 1, e -> e == object2)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 2, e -> e == object2)).isEqualTo(1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 3, e -> e == object2)).isEqualTo(1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 1, 1, e -> e == object2)).isEqualTo(1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 1, 2, e -> e == object2)).isEqualTo(1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 2, 1, e -> e == object2)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 1, e -> e == object3)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 2, e -> e == object3)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 3, e -> e == object3)).isEqualTo(2);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 1, 1, e -> e == object3)).isEqualTo(-1);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 1, 2, e -> e == object3)).isEqualTo(2);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 2, 1, e -> e == object3)).isEqualTo(2);
        assertThat(findIndexRange(new Object[] { object1, object2, object3 }, 0, 3, e -> e == object4)).isEqualTo(-1);
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

        checkCopyElements(create, copy, elementGetter, numElementsGetter, (e, i) -> e.equals(i));
    }

    protected static <T, U, E> void checkCopyElements(Function<int[], T> create, Function<T, U> copy, ElementGetter<U, E> elementGetter,
            NumElementsGetter<U> numElementsGetter, ElementComparator<E> elementComparator) {

        Objects.requireNonNull(create);
        Objects.requireNonNull(copy);

        assertThatThrownBy(() -> copy.apply(null)).isInstanceOf(NullPointerException.class);

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
}
