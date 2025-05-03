package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableIntToIntMap extends IIntKeyMap, IClearable, IIntKeyMapRemovalMutators, IIntToIntMapRemovalMutators, IIntToIntMapMutators, IIntContainsKeyMap,
        IIntToIntMapGetters {

}
