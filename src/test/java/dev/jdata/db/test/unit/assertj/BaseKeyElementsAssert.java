package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.KeyElements;

abstract class BaseKeyElementsAssert<S extends BaseKeyElementsAssert<S, A>, A extends KeyElements>
        extends BaseAssert<S, A>
        implements IKeyElementsAssert<S> {

    BaseKeyElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    @Override
    public final S hasNumKeys(int expectedNumKeys) {

        isNotNull();

        final int actualNumKeys = actual.getNumKeys();

        if (actualNumKeys != expectedNumKeys) {

            failWithActualExpectedAndMessage(actualNumKeys, expectedNumKeys, null);
        }

        return getThis();
    }
}
