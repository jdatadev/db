package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

public interface IMutableBaseMap<T extends IAnyOrderAddable> extends IMutableElements, IMutableMapType, IBaseMapCommon<T> {

}
