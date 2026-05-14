package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.builders.ICachedContainsBuilderMarker;

public interface ICachedIndexListBuilder<T> extends IIndexListBuilder<T, ICachedIndexList<T>, IHeapIndexList<T>>, ICachedContainsBuilderMarker {

}
