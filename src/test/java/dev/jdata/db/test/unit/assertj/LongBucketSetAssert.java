package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.sets.MutableLongBucketSet;

public final class LongBucketSetAssert extends BaseElementsAssert<LongBucketSetAssert, MutableLongBucketSet> {

    LongBucketSetAssert(MutableLongBucketSet actual) {
        super(actual, LongBucketSetAssert.class);
    }

    public final LongBucketSetAssert contains(long value) {

        isNotNull();

        if (!actual.contains(value)) {

            failWithContains(value);
        }

        return this;
    }

    public final LongBucketSetAssert containsExactlyInAnyOrder(long ... values) {

        isNotNull();

        hasNumElements(values.length);

        for (long value : values) {

            if (!actual.contains(value)) {

                failWithContains(value);
            }
        }

        return this;
    }

    public final LongBucketSetAssert doesNotContain(long value) {

        isNotNull();

        if (actual.contains(value)) {

            failWithDoesNotContain(value);
        }

        return this;
    }
}
