package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.jdk.adt.strings.CharSequences;

public final class CharSequenceAssert<A extends CharSequence> extends BaseAssert<CharSequenceAssert<A>, A> {

    protected CharSequenceAssert(A actual) {
        super(actual, castAssertClass(CharSequenceAssert.class));
    }

    public CharSequenceAssert<A> isEqualToCharSequence(CharSequence expected) {

        isNotNull();

        if (!CharSequences.areEqual(actual, expected)) {

            failWithActualExpected(actual, expected);
        }

        return getThis();
    }
}
