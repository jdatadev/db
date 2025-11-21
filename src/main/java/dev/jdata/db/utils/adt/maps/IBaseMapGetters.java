package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

interface IBaseMapGetters<T extends IAnyOrderAddable> extends IMapGettersMarker {

    long keys(T addable);
}
