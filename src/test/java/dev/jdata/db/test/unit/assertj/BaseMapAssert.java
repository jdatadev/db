package dev.jdata.db.test.unit.assertj;

import dev.jdata.db.utils.adt.maps.IBaseMapView;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

abstract class BaseMapAssert<KEYS, KEYS_ADDABLE extends IAnyOrderAddable, S extends BaseMapAssert<KEYS, KEYS_ADDABLE, S, A>, A extends IBaseMapView<KEYS_ADDABLE>>

        extends BaseOnlyElementsAssert<S, A> {

    BaseMapAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    public final S hasNumKeys(long expectedNumKeys) {

        isNotNull();

        final long actualNumKeys = actual.getNumKeys();

        if (actualNumKeys != expectedNumKeys) {

            failWithActualExpectedAndMessage(actualNumKeys, expectedNumKeys, null);
        }

        return getThis();
    }
}
