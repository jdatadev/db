package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IElementsIterable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

abstract class BaseIterableOnlyElementsAssert<S extends BaseOnlyElementsAssert<S, A>, A extends IOnlyElementsView & IElementsIterable> extends BaseOnlyElementsAssert<S, A> {

    BaseIterableOnlyElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }
}
