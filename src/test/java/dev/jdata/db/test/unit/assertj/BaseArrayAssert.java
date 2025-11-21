package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.arrays.IArrayView;

abstract class BaseArrayAssert<S extends BaseArrayAssert<S, A>, A extends IArrayView> extends BaseAssert<S, A> {

    BaseArrayAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }
}
