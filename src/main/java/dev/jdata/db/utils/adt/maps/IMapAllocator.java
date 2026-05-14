package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IElementsAllocator;
import dev.jdata.db.utils.adt.elements.IElementsBuilder;

interface IMapAllocator<T extends IBaseMap<?>, U extends IMutableBaseMap<?>, V extends IElementsBuilder<T, ?>> extends IElementsAllocator<T, U, V> {

}
