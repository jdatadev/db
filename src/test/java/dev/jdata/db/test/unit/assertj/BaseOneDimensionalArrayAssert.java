package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.arrays.IOneDimensionalArrayView;

abstract class BaseOneDimensionalArrayAssert<S extends BaseOneDimensionalArrayAssert<S, A>, A extends IOneDimensionalArrayView> extends BaseArrayAssert<S, A> {

    BaseOneDimensionalArrayAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    public final S isEmpty() {

        isNotNull();

        if (actual.getLimit() != 0L || !actual.isEmpty()) {

            failWithMessage("Expected to be empty");
        }

        return getThis();
    }

    public final S isNotEmpty() {

        isNotNull();

        if (actual.getLimit() == 0L || actual.isEmpty()) {

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
