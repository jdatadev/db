package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.sets.IntSet;

public final class IntSetAssert extends BaseElementsAssert<IntSetAssert, IntSet> {

    IntSetAssert(IntSet actual) {
        super(actual, IntSetAssert.class);
    }

    public final IntSetAssert contains(int value) {

        isNotNull();

        if (!actual.contains(value)) {

            failWithContains(value);
        }

        return this;
    }

    public final IntSetAssert doesNotContain(int value) {

        isNotNull();

        if (actual.contains(value)) {

            failWithDoesNotContain(value);
        }

        return this;
    }
}
