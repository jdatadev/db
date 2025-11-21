package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;

interface IIntSetBuilder<T extends IIntSet, U extends IBaseIntSet & IHeapContainsMarker> extends IBaseIntSetBuilder<T, U> {

}
