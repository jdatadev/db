package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public final class ObjectIterableOnlyElementsAssert<T, U extends IOnlyElementsView & IObjectIterableElementsView<T>>

        extends BaseObjectIterableOnlyElementsAssert<T, ObjectIterableOnlyElementsAssert<T, U>, U> {

    ObjectIterableOnlyElementsAssert(U actual) {
        super(actual, castAssertClass(ObjectIterableOnlyElementsAssert.class));
    }
}
