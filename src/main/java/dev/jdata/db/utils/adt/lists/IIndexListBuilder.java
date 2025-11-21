package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;

public interface IIndexListBuilder<T, U extends IBaseIndexList<T>, V extends IIndexList<T> & IHeapContainsMarker> extends IBaseObjectIndexListBuilder<T, U, V> {

}
