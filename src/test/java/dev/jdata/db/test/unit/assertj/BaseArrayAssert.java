package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.arrays.IArrayCommon;

abstract class BaseArrayAssert<S extends BaseArrayAssert<S, A>, A extends IArrayCommon> extends BaseAssert<S, A> {

    BaseArrayAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }
}
