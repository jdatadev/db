package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.BaseElementsAggregatesTest;

abstract class BaseObjectArrayListTest<T> extends BaseElementsAggregatesTest<T> {

    @SafeVarargs
    final <E, L extends BaseObjectArrayList<E>> void checkElementsSameAs(L list, E ... expectedElements) {

        final int numElements = expectedElements.length;

        checkNumElements(list, numElements);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(list.get(i)).isSameAs(expectedElements[i]);
        }
    }

    protected void checkNumElements(BaseObjectArrayList<?> list, int expectedNumElements) {

        assertThat(list.isEmpty()).isEqualTo(expectedNumElements == 0);
        assertThat(list.getNumElements()).isEqualTo(expectedNumElements);
    }
}
