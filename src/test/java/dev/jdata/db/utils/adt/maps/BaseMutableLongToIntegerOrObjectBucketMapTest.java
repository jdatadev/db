package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;

abstract class BaseMutableLongToIntegerOrObjectBucketMapTest<

                VALUES_ARRAY,
                VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct,
                MAP extends IMutableLongKeyMap & ILongContainsKeyMapView & ILongKeyDynamicMapRemovalMutators>

        extends BaseMutableLongToIntegerOrObjectDynamicMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

}
