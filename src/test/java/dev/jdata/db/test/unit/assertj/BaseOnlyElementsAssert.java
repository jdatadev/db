package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public abstract class BaseOnlyElementsAssert<S extends BaseOnlyElementsAssert<S, A>, A extends IOnlyElementsView> extends BaseElementsAssert<S, A> {

    protected BaseOnlyElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    public final S hasNumElements(long expectedNumElements) {

        isNotNull();

        final long actualNumElements = actual.getNumElements();

        if (actualNumElements != expectedNumElements) {

            failWithActualExpected(actualNumElements, expectedNumElements);
        }

        return getThis();
    }
}
