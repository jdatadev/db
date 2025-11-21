package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.maps.IBaseMapView;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

public final class KeyMapAssert<KEYS, KEYS_ADDABLE extends IAnyOrderAddable, ACTUAL extends IBaseMapView<KEYS_ADDABLE>>

        extends BaseMapAssert<KEYS, KEYS_ADDABLE, KeyMapAssert<KEYS, KEYS_ADDABLE, ACTUAL>, ACTUAL> {

    KeyMapAssert(ACTUAL actual) {
        super(actual, castAssertClass(KeyMapAssert.class));
    }
}
