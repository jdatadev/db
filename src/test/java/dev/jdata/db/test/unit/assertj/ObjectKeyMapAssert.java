package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.maps.IObjectKeyMapView;

public final class ObjectKeyMapAssert<K, A extends IObjectKeyMapView<K>> extends BaseObjectMapAssert<K, ObjectKeyMapAssert<K, A>, A> {

    ObjectKeyMapAssert(A actual) {
        super(actual, castAssertClass(ObjectKeyMapAssert.class));
    }
}
