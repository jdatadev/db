package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface IIntSetBuilder<T extends IIntSet, U extends IIntSet & IHeapContainsMarker> extends IBaseIntSetBuilder<T, U> {

}
