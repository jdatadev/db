package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.sets.IIntSetView;

public final class IntSetViewAssert extends BaseOnlyElementsAssert<IntSetViewAssert, IIntSetView> {

    IntSetViewAssert(IIntSetView actual) {
        super(actual, IntSetViewAssert.class);
    }

    public IntSetViewAssert contains(int value) {

        isNotNull();

        if (!actual.contains(value)) {

            failWithContains(value);
        }

        return this;
    }

    public IntSetViewAssert doesNotContain(int value) {

        isNotNull();

        if (actual.contains(value)) {

            failWithDoesNotContain(value);
        }

        return this;
    }
}
