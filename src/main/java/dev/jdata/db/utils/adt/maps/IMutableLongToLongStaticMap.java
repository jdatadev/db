package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLongToLongStaticMap

        extends ILongKeyMap, IClearable, ILongToLongCommonMapMutators, ILongToLongCommonMapGetters, ILongKeyStaticMapRemovalMutators, ILongToLongStaticMapRemovalMutators {
}
