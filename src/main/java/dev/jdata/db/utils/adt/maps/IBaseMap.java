package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

public interface IBaseMap<T extends IAnyOrderAddable> extends IElements, IMapType, IBaseMapCommon<T> {

}
