package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public final class OnlyElementsAssert extends BaseOnlyElementsAssert<OnlyElementsAssert, IOnlyElementsView> {

    OnlyElementsAssert(IOnlyElementsView actual) {
        super(actual, OnlyElementsAssert.class);
    }
}
