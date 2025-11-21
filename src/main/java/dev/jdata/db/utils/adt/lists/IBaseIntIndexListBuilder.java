package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

interface IBaseIntIndexListBuilder<T extends IBaseIntIndexList, U extends IBaseIntIndexList & IHeapContainsMarker> extends IIntListBuilder<T, U> {

}
