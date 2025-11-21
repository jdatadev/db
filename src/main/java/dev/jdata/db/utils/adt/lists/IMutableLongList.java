package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.checks.Checks;

public interface IMutableLongList extends IMutableLongOrderedOnlyElements, ILongListCommon, IBaseListMutable, IMutableListMarker {

    public static IMutableLongList create(int initialCapacity) {

        Checks.isInitialCapacity(initialCapacity);

        return new MutableLongIndexList(initialCapacity);
    }
}
