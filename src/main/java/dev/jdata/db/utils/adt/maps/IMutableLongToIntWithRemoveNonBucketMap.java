package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLongToIntWithRemoveNonBucketMap extends ILongKeyMap, IClearable, ILongNonContainsMapMutators, ILongToIntMapMutators, ILongToIntMapGetters {

}
