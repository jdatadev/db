package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.arrays.IAnyDimensionalArrayView;

abstract class BaseArrayAssert<S extends BaseArrayAssert<S, A>, A extends IAnyDimensionalArrayView> extends BaseAssert<S, A> {

    BaseArrayAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }
}
