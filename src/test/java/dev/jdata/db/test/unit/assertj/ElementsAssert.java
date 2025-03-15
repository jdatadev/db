package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IElements;

public final class ElementsAssert extends BaseElementsAssert<ElementsAssert, IElements> {

    ElementsAssert(IElements actual) {
        super(actual, ElementsAssert.class);
    }
}
