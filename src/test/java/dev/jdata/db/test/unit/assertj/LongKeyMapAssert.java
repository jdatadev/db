package dev.jdata.db.test.unit.assertj;

import java.util.HashSet;
import java.util.Set;

import dev.jdata.db.utils.adt.maps.ILongContainsKeyMap;

public final class LongKeyMapAssert extends BaseElementsAssert<LongKeyMapAssert, ILongContainsKeyMap> {

    LongKeyMapAssert(ILongContainsKeyMap actual) {
        super(actual, LongKeyMapAssert.class);
    }

    public final LongKeyMapAssert containsKey(long key) {

        isNotNull();

        if (!actual.containsKey(key)) {

            failWithContains(key);
        }

        return this;
    }

    public final LongKeyMapAssert doesNotContainKey(long key) {

        isNotNull();

        if (actual.containsKey(key)) {

            failWithDoesNotContain(key);
        }

        return this;
    }

    public final LongKeyMapAssert hasExactlyKeys(long ... expectedKeys) {

        isNotNull();

        final long[] actualKeys = actual.keys();

        if (!toSet(actualKeys).equals(toSet(expectedKeys))) {

            failWithActualExpected(actualKeys, expectedKeys);
        }

        return this;
    }

    private static Set<Long> toSet(long ... values) {

        final int numValues = values.length;

        final Set<Long> result = new HashSet<>(numValues);

        for (int i = 0; i < numValues; ++ i) {

            result.add(values[i]);
        }

        return result;
    }
}
