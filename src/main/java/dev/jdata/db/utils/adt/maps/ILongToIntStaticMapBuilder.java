package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;

public interface ILongToIntStaticMapBuilder<T extends ILongToIntStaticMap, U extends ILongToIntStaticMap & IHeapContainsMarker> extends IMapBuilder<ILongAnyOrderAddable, T, U> {

}
