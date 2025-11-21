package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IElementsIterable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public abstract class BaseIterableElementsAssert<S extends BaseElementsAssert<S, A>, A extends IElementsIterable & IOnlyElementsView> extends BaseElementsAssert<S, A> {

    BaseIterableElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }
}
