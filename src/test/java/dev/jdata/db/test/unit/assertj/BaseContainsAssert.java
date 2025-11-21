package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.contains.IContainsView;

abstract class BaseContainsAssert<S extends BaseContainsAssert<S, A>, A extends IContainsView> extends BaseAssert<S, A> {

    BaseContainsAssert(A actual, Class<S> assertClass) {
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
}
