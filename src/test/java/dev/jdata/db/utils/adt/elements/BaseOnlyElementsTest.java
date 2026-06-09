package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

abstract class BaseOnlyElementsTest<T extends IOnlyElementsView, U extends IOnlyElementsView> extends BaseElementsAggregatesTest<T> {

    protected void checkNumElements(U elements, int expectedNumElements) {

        Objects.requireNonNull(elements);
        Checks.isIntNumElements(expectedNumElements);

        checkNumElements(elements, IOnlyElementsView::isEmpty, IOnlyElementsView::getNumElements, expectedNumElements);
    }

    static void checkNumOnlyElements(IOnlyElementsView elements, int expectedNumElements) {

        Objects.requireNonNull(elements);
        Checks.isIntNumElements(expectedNumElements);

        checkNumElements(elements, IOnlyElementsView::isEmpty, IOnlyElementsView::getNumElements, expectedNumElements);
    }
}
