package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;

abstract class BaseMutableIntToIntegerOrObjectMaxDistanceMapTest<

                VALUES_ARRAY,
                VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct,
                MAP extends IMutableIntKeyMap & IIntContainsKeyMapView & IIntKeyDynamicMapRemovalMutators>

        extends BaseMutableIntToIntegerOrObjectDynamicMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

}
