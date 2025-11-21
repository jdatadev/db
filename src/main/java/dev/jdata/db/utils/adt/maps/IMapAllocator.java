package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IElementsAllocator;
import dev.jdata.db.utils.adt.elements.IElementsBuilder;

interface IMapAllocator<T extends IBaseMap<?>, U extends IMutableBaseMap<?>> extends IElementsAllocator<T, U, IElementsBuilder<T, ?>> {

}
