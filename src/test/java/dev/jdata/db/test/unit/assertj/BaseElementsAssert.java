package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IElementsView;

abstract class BaseElementsAssert<S extends BaseElementsAssert<S, A>, A extends IElementsView> extends BaseContainsAssert<S, A> {

    BaseElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }
}
