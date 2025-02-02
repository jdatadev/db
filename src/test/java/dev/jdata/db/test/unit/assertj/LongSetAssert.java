package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.sets.LongSet;

public final class LongSetAssert extends BaseElementsAssert<LongSetAssert, LongSet> {

    LongSetAssert(LongSet actual) {
        super(actual, LongSetAssert.class);
    }

    public final LongSetAssert contains(long value) {

        isNotNull();

        if (!actual.contains(value)) {

            failWithContains(value);
        }

        return this;
    }

    public final LongSetAssert containsExactlyInAnyOrder(long ... values) {

        isNotNull();

        hasNumElements(values.length);

        for (long value : values) {

            if (!actual.contains(value)) {

                failWithContains(value);
            }
        }

        return this;
    }

    public final LongSetAssert doesNotContain(long value) {

        isNotNull();

        if (actual.contains(value)) {

            failWithDoesNotContain(value);
        }

        return this;
    }
}
