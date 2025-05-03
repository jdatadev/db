package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableMap<K, V> extends IClearable, IKeyMap<K[]>, IObjectMapMutators<K, V>, IObjectMapGetters<K, V> {

}
