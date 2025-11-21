package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

abstract class BaseMutableIntToIntegerOrObjectMaxDistanceMapTest<

                VALUES_ARRAY,
                VALUES_ADDABLE extends IAnyOrderAddable,
                MAP extends IMutableIntKeyMap & IIntContainsKeyMapView & IIntKeyDynamicMapRemovalMutators>

        extends BaseMutableIntToIntegerOrObjectDynamicMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

}
