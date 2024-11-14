package dev.jdata.db.utils.adt.arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.test.unit.assertj.BaseElementsAssert;
import dev.jdata.db.utils.adt.maps.MapOfList;
import dev.jdata.db.utils.function.BiIntToIntFunction;
import dev.jdata.db.utils.function.IntToIntFunction;

public final class TwoDimensionalArrayTest extends BaseTest {

    private static final int MAX_OUTER_ELEMENTS = 1000;
    private static final int MAX_INNER_ELEMENTS = 10;

    @Test
    @Category(UnitTest.class)
    public void testConstructorArguments() {

        assertThatThrownBy(() -> new TwoDimensionalArray<>(-1,  Integer[][]::new, 1, Integer[]::new)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TwoDimensionalArray<>(0,   Integer[][]::new, 1, Integer[]::new)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TwoDimensionalArray<>(1,   null, 1, Integer[]::new)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new TwoDimensionalArray<>(1,   Integer[][]::new, -1, Integer[]::new)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TwoDimensionalArray<>(1,   Integer[][]::new, 0, Integer[]::new)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TwoDimensionalArray<>(1,   Integer[][]::new, 1, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testAdd() {

        final TwoDimensionalArray<Integer> twoDimensionalArray = createTwoDimensionalArray();

        assertThat(twoDimensionalArray).isEmpty();
        assertThat(twoDimensionalArray).hasNumElements(0);
        assertThat(twoDimensionalArray).hasNumOuterElements(0);

        assertThatThrownBy(() -> twoDimensionalArray.add(1, 1)).isInstanceOf(IndexOutOfBoundsException.class);

        assertThat(twoDimensionalArray).isEmpty();
        assertThat(twoDimensionalArray).hasNumElements(0);
        assertThat(twoDimensionalArray).hasNumOuterElements(0);

        final List<Integer> expectedValues = new ArrayList<>();

        final MapOfList<Integer, Integer> expectedKeyValues = new MapOfList<>(MAX_OUTER_ELEMENTS);

        for (int numOuterElements = 1; numOuterElements < MAX_OUTER_ELEMENTS; numOuterElements *= 10) {

            final int numInnerElements = MAX_INNER_ELEMENTS;

            for (int outerIndex = 0; outerIndex < numOuterElements; ++ outerIndex) {

                for (int innerIndex = 0; innerIndex < numInnerElements; ++ innerIndex) {

                    final int value = makeValue(outerIndex, innerIndex);

                    twoDimensionalArray.add(outerIndex, value);
                }

                assertThat(twoDimensionalArray).isNotEmpty();

                final int numExpectedOuterElements = outerIndex + 1;

                assertThat(twoDimensionalArray).hasNumElements(numExpectedOuterElements * numInnerElements);
                assertThat(twoDimensionalArray).hasNumOuterElements(numExpectedOuterElements);
            }

            for (int outerIndex = 0; outerIndex < numOuterElements; ++ outerIndex) {

                for (int innerIndex = 0; innerIndex < numInnerElements; ++ innerIndex) {

                    final int value = makeValue(outerIndex, innerIndex);

                    expectedValues.add(value);
                }

                final int testValue = -1;

                final List<Integer> unmodifiableList = checkUnmodifiableEmptyIsEmptyList(twoDimensionalArray.toUnmodifiableList(outerIndex), testValue);

                assertThat(unmodifiableList).isEqualTo(expectedValues);
                assertThatThrownBy(() -> unmodifiableList.add(123)).isInstanceOf(UnsupportedOperationException.class);

                expectedValues.clear();
            }

            twoDimensionalArray.forEachElement((i, e) -> expectedKeyValues.add(i, e));

            checkForEach(expectedKeyValues, numOuterElements, numOuterElements * numInnerElements, o -> numInnerElements, (o, i) -> makeValue(o, i));

            expectedKeyValues.clear();

            twoDimensionalArray.clear();

            assertThat(twoDimensionalArray).isEmpty();
            assertThat(twoDimensionalArray).hasNumElements(0);
            assertThat(twoDimensionalArray).hasNumOuterElements(0);
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testAddWithOuterExpand() {

        final TwoDimensionalArray<Integer> twoDimensionalArray = createTwoDimensionalArray();

        assertThat(twoDimensionalArray).isEmpty();
        assertThat(twoDimensionalArray).hasNumElements(0);
        assertThat(twoDimensionalArray).hasNumOuterElements(0);

        twoDimensionalArray.addWithOuterExpand(1, 1);

        assertThat(twoDimensionalArray).isNotEmpty();
        assertThat(twoDimensionalArray).hasNumElements(1);
        assertThat(twoDimensionalArray).hasNumOuterElements(2);

        assertThat(checkUnmodifiableEmptyIsEmptyList(twoDimensionalArray.toUnmodifiableList(0), -1)).isEmpty();

        assertThat(twoDimensionalArray.toUnmodifiableList(1)).containsExactly(1);
    }

    @Test
    @Category(UnitTest.class)
    public void testIsEmpty() {

        final TwoDimensionalArray<Integer> twoDimensionalArray = createTwoDimensionalArray();

        assertThat(twoDimensionalArray).isEmpty();

        twoDimensionalArray.add(0, 1);

        assertThat(twoDimensionalArray).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumElements() {

        final TwoDimensionalArray<Integer> twoDimensionalArray = createTwoDimensionalArray();

        assertThat(twoDimensionalArray).hasNumElements(0);

        twoDimensionalArray.add(0, 1);

        assertThat(twoDimensionalArray).hasNumElements(1);
    }

    @Test
    @Category(UnitTest.class)
    public void testClear() {

        final TwoDimensionalArray<Integer> twoDimensionalArray = createTwoDimensionalArray();

        assertThat(twoDimensionalArray).isEmpty();
        assertThat(twoDimensionalArray).hasNumElements(0);

        twoDimensionalArray.add(0, 1);

        assertThat(twoDimensionalArray).hasNumElements(1);

        twoDimensionalArray.clear();

        assertThat(twoDimensionalArray).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumOuterElements() {

        final TwoDimensionalArray<Integer> twoDimensionalArray = createTwoDimensionalArray();

        assertThat(twoDimensionalArray).hasNumOuterElements(0);

        twoDimensionalArray.add(0, 1);

        assertThat(twoDimensionalArray).hasNumOuterElements(1);
    }

    @Test
    @Category(UnitTest.class)
    public void testFindExactlyOne() {

        final TwoDimensionalArray<String> twoDimensionalArray = createTwoDimensionalArray(String[][]::new, String[]::new);

        assertThatThrownBy(() -> twoDimensionalArray.findExactlyOne(-1, s -> true)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> twoDimensionalArray.findExactlyOne(0, s -> true)).isInstanceOf(IndexOutOfBoundsException.class);

        final int element = 123;
        final int otherElement = 234;
        final int anotherElement = 234;

        final String elementString = String.valueOf(element);

        final int outerIndex0 = 0;

        final Predicate<String> predicate = s -> Integer.parseInt(s) == element;

        twoDimensionalArray.add(outerIndex0, String.valueOf(otherElement));

        assertThatThrownBy(() -> twoDimensionalArray.findExactlyOne(0, null)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> twoDimensionalArray.findExactlyOne(outerIndex0, predicate)).isInstanceOf(NoSuchElementException.class);

        twoDimensionalArray.add(outerIndex0, elementString);
        twoDimensionalArray.add(outerIndex0, String.valueOf(anotherElement));

        assertThat(twoDimensionalArray.findExactlyOne(outerIndex0, predicate)).isSameAs(elementString);

        twoDimensionalArray.add(outerIndex0, String.valueOf(element));

        assertThatThrownBy(() -> twoDimensionalArray.findExactlyOne(outerIndex0, predicate)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testForEachElement() {

        final TwoDimensionalArray<Integer> twoDimensionalArray = createTwoDimensionalArray();

        assertThatThrownBy(() -> twoDimensionalArray.forEachElement(null)).isInstanceOf(NullPointerException.class);

        assertThat(twoDimensionalArray).hasNumOuterElements(0);

        twoDimensionalArray.add(0, 1);
        twoDimensionalArray.add(0, 2);
        twoDimensionalArray.add(1, 3);

        final MapOfList<Integer, Integer> expectedKeyValues = new MapOfList<>(10);

        twoDimensionalArray.forEachElement((i, e) -> expectedKeyValues.add(i, e));

        checkForEach(expectedKeyValues, 2, 3, o -> o == 0 ? 2 : 1, (o, i) -> o == 0 ? (i == 0 ? 1 : 2) : 3);
    }

    private static void checkForEach(MapOfList<Integer, Integer> expectedKeyValues, int numOuterElements, int numElements, IntToIntFunction numInnerElementsGetter,
            BiIntToIntFunction valueGetter) {

        assertThat(expectedKeyValues.isEmpty()).isEqualTo(numOuterElements == 0);
        assertThat(expectedKeyValues).hasNumElements(numElements);
        assertThat(expectedKeyValues).hasNumKeys(numOuterElements);

        final List<Integer> expectedValues = new ArrayList<>();

        for (int outerIndex = 0; outerIndex < numOuterElements; ++ outerIndex) {

            final int numInnerElements = numInnerElementsGetter.apply(outerIndex);

            for (int innerIndex = 0; innerIndex < numInnerElements; ++ innerIndex) {

                final int value = valueGetter.apply(outerIndex, innerIndex);

                expectedValues.add(value);
            }

            assertThat(expectedKeyValues.getUnmodifiable(outerIndex)).isEqualTo(expectedValues);

            expectedValues.clear();
        }
    }

    private static TwoDimensionalArray<Integer> createTwoDimensionalArray() {

        return createTwoDimensionalArray(Integer[][]::new, Integer[]::new);
    }

    private static <T> TwoDimensionalArray<T> createTwoDimensionalArray(IntFunction<T[][]> createOuterArray, IntFunction<T[]> createInnerArray) {

        return new TwoDimensionalArray<>(1, createOuterArray, 1, createInnerArray);
    }

    private static int makeValue(int outerIndex, int innerIndex) {

        return (outerIndex * 1000 * 1000) + innerIndex;
    }

    private static final class TwoDimensionalArrayAssert<T> extends BaseElementsAssert<TwoDimensionalArrayAssert<T>, TwoDimensionalArray<T>> {

        TwoDimensionalArrayAssert(TwoDimensionalArray<T> actual) {
            super(actual, castAssertClass(TwoDimensionalArrayAssert.class));
        }

        TwoDimensionalArrayAssert<T> hasNumOuterElements(int expectedNumOuterElements) {

            isNotNull();

            final int actualNumOuterElements = actual.getNumOuterElements();

            if (actualNumOuterElements != expectedNumOuterElements) {

                failWithActualExpected(actualNumOuterElements, expectedNumOuterElements);
            }

            return this;
        }
    }

    public static <T> TwoDimensionalArrayAssert<T> assertThat(TwoDimensionalArray<T> actual) {

        return new TwoDimensionalArrayAssert<>(actual);
    }
}
