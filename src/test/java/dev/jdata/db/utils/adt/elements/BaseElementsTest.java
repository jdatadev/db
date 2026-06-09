package dev.jdata.db.utils.adt.elements;

import java.util.function.Predicate;
import java.util.function.ToLongFunction;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.function.ObjLongFunction;

public abstract class BaseElementsTest extends BaseTest {

    protected static <T, U> void checkElementsSameAs(U elements, ObjLongFunction<U, T> elementGetter, Predicate<U> isEmpty, ToLongFunction<U> numElementsGetter,
            T[] expectedElements) {

        final int numElements = expectedElements.length;

        checkNumElements(elements, isEmpty, numElementsGetter, numElements);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(elementGetter.apply(elements, i)).isSameAs(expectedElements[i]);
        }
    }

    static <T> void checkNumElements(T elements, Predicate<T> isEmpty, ToLongFunction<T> numElementsGetter, int expectedNumElements) {

        assertThat(isEmpty.test(elements)).isEqualTo(expectedNumElements == 0);
        assertThat(numElementsGetter.applyAsLong(elements)).isEqualTo(expectedNumElements);
    }
}
