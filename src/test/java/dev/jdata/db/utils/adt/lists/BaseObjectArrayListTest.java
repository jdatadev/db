package dev.jdata.db.utils.adt.lists;

import java.util.function.Predicate;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.elements.BaseElementsAggregatesTest;
import dev.jdata.db.utils.function.ObjIntFunction;

abstract class BaseObjectArrayListTest<T> extends BaseElementsAggregatesTest<T> {

    @SafeVarargs
    static <E, L extends BaseObjectArrayList<E>> void checkElementsSameAs(L list, E ... expectedElements) {

        checkElementsSameAs(list, BaseObjectArrayList::get, BaseObjectArrayList::isEmpty, BaseObjectArrayList::getNumElements, expectedElements);
    }

    protected void checkNumElements(BaseObjectArrayList<?> list, int expectedNumElements) {

        checkNumElements(list, BaseObjectArrayList::isEmpty, BaseObjectArrayList::getNumElements, expectedNumElements);
    }

    static <E, L> void checkElementsSameAs(L list, ObjIntFunction<L, E> elementGetter, Predicate<L> isEmpty, ToLongFunction<L> numElementsGetter, E[] expectedElements) {

        final int numElements = expectedElements.length;

        checkNumElements(list, isEmpty, numElementsGetter, numElements);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(elementGetter.apply(list, i)).isSameAs(expectedElements[i]);
        }
    }

    static <L> void checkNumElements(L list, Predicate<L> isEmpty, ToLongFunction<L> numElementsGetter, int expectedNumElements) {

        assertThat(isEmpty.test(list)).isEqualTo(expectedNumElements == 0);
        assertThat(numElementsGetter.applyAsLong(list)).isEqualTo(expectedNumElements);
    }
}
