package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.BaseADTElements;

abstract class BaseMutableLongToObjectDynamicMapAllocator<V, T extends IBaseMutableLongToObjectDynamicMap<V>, U extends BaseADTElements<Void, U, Void> & IMutableMapType>

        extends BaseMutableMapAllocator<T, U, ILongToObjectMapView<V>>
        implements IBaseMutableLongToObjectDynamicMapAllocator<V, T> {

}
