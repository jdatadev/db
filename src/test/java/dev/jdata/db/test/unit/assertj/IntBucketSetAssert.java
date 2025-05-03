package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;

public final class IntBucketSetAssert extends BaseElementsAssert<IntBucketSetAssert, MutableIntBucketSet> {

    IntBucketSetAssert(MutableIntBucketSet actual) {
        super(actual, IntBucketSetAssert.class);
    }

    public final IntBucketSetAssert contains(int value) {

        isNotNull();

        if (!actual.contains(value)) {

            failWithContains(value);
        }

        return this;
    }

    public final IntBucketSetAssert doesNotContain(int value) {

        isNotNull();

        if (actual.contains(value)) {

            failWithDoesNotContain(value);
        }

        return this;
    }
}
