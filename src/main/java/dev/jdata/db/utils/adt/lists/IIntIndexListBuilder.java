package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface IIntIndexListBuilder<T extends IIntIndexList, U extends IIntIndexList & IHeapContainsMarker> extends IBaseIntIndexListBuilder<T, U> {

}
