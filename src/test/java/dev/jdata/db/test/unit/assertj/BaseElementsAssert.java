package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.elements.IElements;

public abstract class BaseElementsAssert<S extends BaseElementsAssert<S, A>, A extends IElements> extends BaseAssert<S, A> {

    protected BaseElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    public final S isEmpty() {

        isNotNull();

        if (!actual.isEmpty()) {

            failWithMessage("Expected to be empty");
        }

        return getThis();
    }

    public final S isNotEmpty() {

        isNotNull();

        if (actual.isEmpty()) {

            failWithMessage("Expected to not be empty");
        }

        return getThis();
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
