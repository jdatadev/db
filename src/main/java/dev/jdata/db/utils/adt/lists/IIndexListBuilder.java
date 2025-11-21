package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface IIndexListBuilder<T, U extends IIndexList<T>, V extends IIndexList<T> & IHeapContainsMarker> extends IBaseObjectIndexListBuilder<T, U, V> {

}
