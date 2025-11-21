package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.arrays.IOneDimensionalArrayView;

public final class OneDimensionalArrayAssert extends BaseOneDimensionalArrayAssert<OneDimensionalArrayAssert, IOneDimensionalArrayView> {

    OneDimensionalArrayAssert(IOneDimensionalArrayView actual) {
        super(actual, OneDimensionalArrayAssert.class);
    }
}
