package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableIntToObjectStaticMap<T>

        extends IIntToObjectStaticMapCommon<T>, IClearable, IIntToObjectCommonMapMutators<T>, IIntKeyStaticMapRemovalMutators, IIntToObjectStaticMapRemovalMutators<T> {
}
