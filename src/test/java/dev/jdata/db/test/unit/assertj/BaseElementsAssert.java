package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.Elements;

public abstract class BaseElementsAssert<S extends BaseElementsAssert<S, A>, A extends Elements> extends BaseAssert<S, A> {

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

    public final S hasNumElements(int expectedNumElements) {

        isNotNull();

        final int actualNumElements = actual.getNumElements();

        if (actualNumElements != expectedNumElements) {

            failWithActualExpected(actualNumElements, expectedNumElements);
        }

        return getThis();
    }
}