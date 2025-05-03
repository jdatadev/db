package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.arrays.IArray;

public final class ArrayAssert extends BaseArrayAssert<ArrayAssert, IArray> {

    ArrayAssert(IArray actual) {
        super(actual, ArrayAssert.class);
    }
}
