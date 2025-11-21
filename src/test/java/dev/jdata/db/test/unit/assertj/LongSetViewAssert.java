package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.sets.ILongSetView;

public final class LongSetViewAssert extends BaseOnlyElementsAssert<LongSetViewAssert, ILongSetView> {

    LongSetViewAssert(ILongSetView actual) {
        super(actual, LongSetViewAssert.class);
    }

    public LongSetViewAssert contains(long value) {

        isNotNull();

        if (!actual.contains(value)) {

            failWithContains(value);
        }

        return this;
    }

    public LongSetViewAssert containsExactlyInAnyOrder(long ... values) {

        isNotNull();

        hasNumElements(values.length);

        for (long value : values) {

            if (!actual.contains(value)) {

                failWithContains(value);
            }
        }

        return this;
    }

    public LongSetViewAssert doesNotContain(long value) {

        isNotNull();

        if (actual.contains(value)) {

            failWithDoesNotContain(value);
        }

        return this;
    }
}
