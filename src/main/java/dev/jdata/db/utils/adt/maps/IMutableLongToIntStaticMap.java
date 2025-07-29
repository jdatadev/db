package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLongToIntStaticMap

        extends IBaseLongToIntMapCommon<ILongToIntStaticMapCommon>,
                IClearable,
                ILongToIntCommonMapMutators,
                ILongKeyStaticMapRemovalMutators,
                ILongToIntStaticMapRemovalMutators {
}
