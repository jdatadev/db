package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public final class ElementsAssert extends BaseElementsAssert<ElementsAssert, IOnlyElementsView> {

    ElementsAssert(IOnlyElementsView actual) {
        super(actual, ElementsAssert.class);
    }
}
