package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IOnlyElementsBuilder;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

interface IMapBuilder<T extends IAnyOrderAddable, U extends IBaseMap<T>, V extends IBaseMap<T> & IHeapContainsMarker> extends IOnlyElementsBuilder<U, V> {

}
