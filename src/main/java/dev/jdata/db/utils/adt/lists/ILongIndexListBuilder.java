package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface ILongIndexListBuilder<T extends ILongIndexList, U extends ILongIndexList & IHeapContainsMarker> extends IBaseLongIndexListBuilder<T, U> {

}
