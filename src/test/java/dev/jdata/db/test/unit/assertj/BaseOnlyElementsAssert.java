package dev.jdata.db.test.unit.assertj;

import static org.junit.Assert.assertNotNull;

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

    public final S isSameNumElements(IOnlyElementsView other) {

        isNotNull();
        assertNotNull(other);

        final long actualNumElements = actual.getNumElements();
        final long otherNumElements = other.getNumElements();

        if (actualNumElements != otherNumElements) {

            failWithActualExpected(actualNumElements, otherNumElements);
        }

        return getThis();
    }
}
