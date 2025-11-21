package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;

interface IBaseObjectIndexListBuilder<T, U extends IBaseIndexList<T>, V extends IBaseIndexList<T> & IHeapContainsMarker> extends IObjectListBuilder<T, U, V> {

}
