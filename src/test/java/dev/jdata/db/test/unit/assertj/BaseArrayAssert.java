package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.arrays.IArray;

abstract class BaseArrayAssert<S extends BaseArrayAssert<S, A>, A extends IArray> extends BaseAssert<S, A> {

    protected BaseArrayAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    public final S isEmpty() {

        isNotNull();

        if (actual.getLimit() != 0L) {

            failWithMessage("Expected to be empty");
        }

        return getThis();
    }

    public final S isNotEmpty() {

        isNotNull();

        if (actual.getLimit() != 0L) {

            failWithMessage("Expected to not be empty");
        }

        return getThis();
    }

    public final S hasLimit(long expectedLimit) {

        isNotNull();

        final long actualLimit = actual.getLimit();

        if (actualLimit != expectedLimit) {

            failWithActualExpected(actualLimit, expectedLimit);
        }

        return getThis();
    }
}
