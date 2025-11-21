package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;

abstract class BaseMutableIntToIntegerOrObjectNonBucketMapTest<VALUES_ARRAY, VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct, MAP extends IMutableIntKeyMap>

        extends BaseMutableIntToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

}
