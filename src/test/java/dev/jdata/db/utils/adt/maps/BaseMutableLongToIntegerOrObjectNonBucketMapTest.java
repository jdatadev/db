package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableLongToIntegerOrObjectNonBucketMapTest<VALUES_ARRAY, VALUES_ADDABLE extends IAnyOrderAddable, MAP extends IMutableLongKeyMap>

        extends BaseMutableLongToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

    @Override
    final int getKey(long[] keys, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(keys[index]);
    }
}
