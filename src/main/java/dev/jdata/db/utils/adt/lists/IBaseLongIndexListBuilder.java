package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

interface IBaseLongIndexListBuilder<T extends IBaseLongIndexList, U extends IBaseLongIndexList & IHeapContainsMarker> extends ILongListBuilder<T, U> {

}
