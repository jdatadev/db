package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLongToIntMap extends ILongKeyMap, IClearable, ILongKeyDynamicMapRemovalMutators, ILongToIntDynamicMapRemovalMutators, ILongToIntCommonMapMutators, ILongToIntCommonMapGetters {

}
