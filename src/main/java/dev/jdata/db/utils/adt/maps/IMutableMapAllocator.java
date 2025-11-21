package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.IMutableOnlyElementsAllocator;

interface IMutableMapAllocator<T extends IMutableBaseMap<?>, U extends IMutableFrom> extends IMutableOnlyElementsAllocator<T, U> {

}
