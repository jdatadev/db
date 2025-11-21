package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

interface IBaseObjectIndexListBuilder<T, U extends IBaseObjectIndexList<T>, V extends IBaseObjectIndexList<T> & IHeapContainsMarker> extends IObjectListBuilder<T, U, V> {

}
