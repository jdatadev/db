package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

interface ILongSetBuilder<T extends ILongSet, U extends ILongSet & IHeapContainsMarker> extends IBaseLongSetBuilder<T, U> {

}
