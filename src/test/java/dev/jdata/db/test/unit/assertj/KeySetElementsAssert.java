package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.KeySetElements;

public final class KeySetElementsAssert<K> extends BaseKeySetElementsAssert<KeySetElementsAssert<K>, KeySetElements<K>, K> {

    KeySetElementsAssert(KeySetElements<K> actual) {
        super(actual, castAssertClass(KeySetElementsAssert.class));
    }
}
