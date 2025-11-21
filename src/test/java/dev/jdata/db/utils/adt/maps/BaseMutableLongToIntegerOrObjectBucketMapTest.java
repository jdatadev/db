package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

abstract class BaseMutableLongToIntegerOrObjectBucketMapTest<

                VALUES_ARRAY,
                VALUES_ADDABLE extends IAnyOrderAddable,
                MAP extends IMutableLongKeyMap & ILongContainsKeyMapView & ILongKeyDynamicMapRemovalMutators>

        extends BaseMutableLongToIntegerOrObjectDynamicMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

}
